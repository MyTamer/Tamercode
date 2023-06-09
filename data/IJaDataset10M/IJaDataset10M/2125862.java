package org.pdfclown.documents.interaction.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.pdfclown.PDF;
import org.pdfclown.VersionEnum;
import org.pdfclown.documents.Document;
import org.pdfclown.documents.contents.layers.Layer;
import org.pdfclown.objects.PdfArray;
import org.pdfclown.objects.PdfDirectObject;
import org.pdfclown.objects.PdfName;
import org.pdfclown.objects.PdfObjectWrapper;
import org.pdfclown.util.NotImplementedException;

/**
  'Set the state of one or more optional content groups' action [PDF:1.6:8.5.3].

  @author Stefano Chizzolini (http://www.stefanochizzolini.it)
  @since 0.0.7
  @version 0.1.1, 11/01/11
*/
@PDF(VersionEnum.PDF15)
public final class SetLayerState extends Action {

    public enum StateModeEnum {

        On(PdfName.ON), Off(PdfName.OFF), Toggle(PdfName.Toggle);

        public static StateModeEnum valueOf(PdfName name) {
            if (name == null) return null;
            for (StateModeEnum value : values()) {
                if (value.getName().equals(name)) return value;
            }
            throw new UnsupportedOperationException("State mode unknown: " + name);
        }

        private PdfName name;

        private StateModeEnum(PdfName name) {
            this.name = name;
        }

        public PdfName getName() {
            return name;
        }
    }

    public static class LayerState {

        private static class Layers extends ArrayList<Layer> {

            private static final long serialVersionUID = 1L;

            private LayerState parentState;

            @Override
            public boolean add(Layer item) {
                super.add(item);
                LayerStates baseStates = getBaseStates();
                if (baseStates != null) {
                    int baseIndex = baseStates.getBaseIndex(parentState);
                    int itemIndex = baseIndex + 1 + size() - 1;
                    baseStates.getBaseDataObject().set(itemIndex, item.getBaseObject());
                }
                return true;
            }

            @Override
            public void add(int index, Layer item) {
                super.add(index, item);
                LayerStates baseStates = getBaseStates();
                if (baseStates != null) {
                    int baseIndex = baseStates.getBaseIndex(parentState);
                    int itemIndex = baseIndex + 1 + index;
                    baseStates.getBaseDataObject().set(itemIndex, item.getBaseObject());
                }
            }

            @Override
            public boolean addAll(Collection<? extends Layer> items) {
                for (Layer item : items) {
                    add(item);
                }
                return !items.isEmpty();
            }

            @Override
            public boolean addAll(int index, Collection<? extends Layer> items) {
                for (Layer item : items) {
                    add(index++, item);
                }
                return !items.isEmpty();
            }

            @Override
            public void clear() {
                LayerStates baseStates = getBaseStates();
                if (baseStates != null) {
                    int itemIndex = baseStates.getBaseIndex(parentState) + 1;
                    for (int count = size(); count > 0; count--) {
                        baseStates.getBaseDataObject().remove(itemIndex);
                    }
                }
                super.clear();
            }

            @Override
            public boolean remove(Object item) {
                int index = indexOf(item);
                if (index == -1) return false;
                remove(index);
                return true;
            }

            @Override
            public Layer remove(int index) {
                Layer layer = super.remove(index);
                LayerStates baseStates = getBaseStates();
                if (baseStates != null) {
                    int baseIndex = baseStates.getBaseIndex(parentState);
                    int itemIndex = baseIndex + 1 + index;
                    baseStates.getBaseDataObject().remove(itemIndex);
                }
                return layer;
            }

            @Override
            public boolean removeAll(Collection<?> items) {
                boolean changed = false;
                for (Object item : items) {
                    changed |= remove(item);
                }
                return changed;
            }

            @Override
            public boolean retainAll(Collection<?> items) {
                throw new NotImplementedException();
            }

            @Override
            public Layer set(int index, Layer item) {
                Layer oldLayer = remove(index);
                add(index, item);
                return oldLayer;
            }

            private LayerStates getBaseStates() {
                return parentState != null ? parentState.baseStates : null;
            }
        }

        private final Layers layers;

        private StateModeEnum mode;

        private LayerStates baseStates;

        public LayerState(StateModeEnum mode) {
            this(mode, new Layers(), null);
        }

        LayerState(StateModeEnum mode, Layers layers, LayerStates baseStates) {
            this.mode = mode;
            this.layers = layers;
            this.layers.parentState = this;
            attach(baseStates);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof LayerState)) return false;
            LayerState state = (LayerState) obj;
            if (!state.getMode().equals(getMode()) || state.getLayers().size() != getLayers().size()) return false;
            Iterator<Layer> layerIterator = getLayers().iterator();
            Iterator<Layer> stateLayerIterator = state.getLayers().iterator();
            while (layerIterator.hasNext()) {
                if (!layerIterator.next().equals(stateLayerIterator.next())) return false;
            }
            return true;
        }

        public List<Layer> getLayers() {
            return layers;
        }

        public StateModeEnum getMode() {
            return mode;
        }

        @Override
        public int hashCode() {
            return mode.hashCode() ^ layers.size();
        }

        public void setMode(StateModeEnum value) {
            mode = value;
            if (baseStates != null) {
                int baseIndex = baseStates.getBaseIndex(this);
                baseStates.getBaseDataObject().set(baseIndex, value.getName());
            }
        }

        void attach(LayerStates baseStates) {
            this.baseStates = baseStates;
        }

        void detach() {
            baseStates = null;
        }
    }

    public static class LayerStates extends PdfObjectWrapper<PdfArray> implements List<LayerState> {

        private List<LayerState> items;

        public LayerStates() {
            super(new PdfArray());
        }

        public LayerStates(PdfDirectObject baseObject) {
            super(baseObject);
            initialize();
        }

        @Override
        public Object clone(Document context) {
            throw new NotImplementedException();
        }

        @Override
        public boolean add(LayerState item) {
            PdfArray baseDataObject = getBaseDataObject();
            baseDataObject.add(item.getMode().getName());
            for (Layer layer : item.getLayers()) {
                baseDataObject.add(layer.getBaseObject());
            }
            items.add(item);
            item.attach(this);
            return true;
        }

        @Override
        public void add(int index, LayerState item) {
            int baseIndex = getBaseIndex(index);
            if (baseIndex == -1) {
                add(item);
            } else {
                PdfArray baseDataObject = getBaseDataObject();
                baseDataObject.add(baseIndex++, item.getMode().getName());
                for (Layer layer : item.getLayers()) {
                    baseDataObject.add(baseIndex++, layer.getBaseObject());
                }
                items.add(index, item);
                item.attach(this);
            }
        }

        @Override
        public boolean addAll(Collection<? extends LayerState> items) {
            for (LayerState item : items) {
                add(item);
            }
            return !items.isEmpty();
        }

        @Override
        public boolean addAll(int index, Collection<? extends LayerState> items) {
            for (LayerState item : items) {
                add(index++, item);
            }
            return !items.isEmpty();
        }

        @Override
        public void clear() {
            getBaseDataObject().clear();
            for (LayerState item : items) {
                item.detach();
            }
            items.clear();
        }

        @Override
        public boolean contains(Object item) {
            return items.contains(item);
        }

        @Override
        public boolean containsAll(Collection<?> items) {
            return this.items.containsAll(items);
        }

        @Override
        public LayerState get(int index) {
            return items.get(index);
        }

        @Override
        public int indexOf(Object item) {
            return items.indexOf(item);
        }

        @Override
        public boolean isEmpty() {
            return items.isEmpty();
        }

        @Override
        public Iterator<LayerState> iterator() {
            return items.iterator();
        }

        @Override
        public int lastIndexOf(Object item) {
            return items.lastIndexOf(item);
        }

        @Override
        public ListIterator<LayerState> listIterator() {
            return items.listIterator();
        }

        @Override
        public ListIterator<LayerState> listIterator(int index) {
            return items.listIterator(index);
        }

        @Override
        public boolean remove(Object item) {
            int index = indexOf(item);
            if (index == -1) return false;
            remove(index);
            return true;
        }

        @Override
        public LayerState remove(int index) {
            LayerState layerState;
            {
                int baseIndex = getBaseIndex(index);
                if (baseIndex == -1) throw new IndexOutOfBoundsException();
                PdfArray baseDataObject = getBaseDataObject();
                boolean done = false;
                for (int baseCount = baseDataObject.size(); baseIndex < baseCount; ) {
                    if (baseDataObject.get(baseIndex) instanceof PdfName) {
                        if (done) break;
                        done = true;
                    }
                    baseDataObject.remove(baseIndex);
                }
            }
            {
                layerState = items.remove(index);
                layerState.detach();
            }
            return layerState;
        }

        @Override
        public boolean removeAll(Collection<?> items) {
            boolean changed = false;
            for (Object item : items) {
                if (remove(item)) {
                    changed = true;
                }
            }
            return changed;
        }

        @Override
        public boolean retainAll(Collection<?> items) {
            throw new NotImplementedException();
        }

        @Override
        public LayerState set(int index, LayerState item) {
            LayerState oldLayerState = remove(index);
            add(index, item);
            return oldLayerState;
        }

        @Override
        public int size() {
            return items.size();
        }

        @Override
        public List<LayerState> subList(int fromIndex, int toIndex) {
            throw new NotImplementedException();
        }

        @Override
        public Object[] toArray() {
            throw new NotImplementedException();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            throw new NotImplementedException();
        }

        /**
      Gets the position of the initial base item corresponding to the specified layer state index.

      @param index Layer state index.
      @return -1, in case <code>index</code> is outside the available range.
    */
        private int getBaseIndex(int index) {
            int baseIndex = -1;
            {
                PdfArray baseDataObject = getBaseDataObject();
                int layerStateIndex = -1;
                for (int baseItemIndex = 0, baseItemCount = baseDataObject.size(); baseItemIndex < baseItemCount; baseItemIndex++) {
                    if (baseDataObject.get(baseItemIndex) instanceof PdfName) {
                        layerStateIndex++;
                        if (layerStateIndex == index) {
                            baseIndex = baseItemIndex;
                            break;
                        }
                    }
                }
            }
            return baseIndex;
        }

        /**
      Gets the position of the initial base item corresponding to the specified layer state.

      @param item Layer state.
      @return -1, in case <code>item</code> has no match.
    */
        private int getBaseIndex(LayerState item) {
            int baseIndex = -1;
            {
                PdfArray baseDataObject = getBaseDataObject();
                for (int baseItemIndex = 0, baseItemCount = baseDataObject.size(); baseItemIndex < baseItemCount; baseItemIndex++) {
                    PdfDirectObject baseItem = baseDataObject.get(baseItemIndex);
                    if (baseItem instanceof PdfName && baseItem.equals(item.getMode().getName())) {
                        for (Layer layer : item.getLayers()) {
                            if (++baseItemIndex >= baseItemCount) break;
                            baseItem = baseDataObject.get(baseItemIndex);
                            if (baseItem instanceof PdfName || !baseItem.equals(layer.getBaseObject())) break;
                        }
                    }
                }
            }
            return baseIndex;
        }

        private void initialize() {
            items = new ArrayList<SetLayerState.LayerState>();
            PdfArray baseDataObject = getBaseDataObject();
            StateModeEnum mode = null;
            LayerState.Layers layers = null;
            for (int baseIndex = 0, baseCount = baseDataObject.size(); baseIndex < baseCount; baseIndex++) {
                PdfDirectObject baseObject = baseDataObject.get(baseIndex);
                if (baseObject instanceof PdfName) {
                    if (mode != null) {
                        items.add(new LayerState(mode, layers, this));
                    }
                    mode = StateModeEnum.valueOf((PdfName) baseObject);
                    layers = new LayerState.Layers();
                } else {
                    layers.add(new Layer(baseObject));
                }
            }
            if (mode != null) {
                items.add(new LayerState(mode, layers, this));
            }
        }
    }

    /**
    Creates a new action within the given document context.
  */
    public SetLayerState(Document context) {
        super(context, PdfName.SetOCGState);
        setStates(new LayerStates());
    }

    SetLayerState(PdfDirectObject baseObject) {
        super(baseObject, null);
    }

    @Override
    public SetLayerState clone(Document context) {
        throw new NotImplementedException();
    }

    public LayerStates getStates() {
        return new LayerStates(getBaseDataObject().get(PdfName.State));
    }

    public void setStates(LayerStates value) {
        getBaseDataObject().put(PdfName.State, value.getBaseObject());
    }
}
