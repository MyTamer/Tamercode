package com.dyuproject.protostuff.model;

public final class V2Speed {

    private V2Speed() {
    }

    public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
    }

    public static final class Person extends com.google.protobuf.GeneratedMessage {

        private Person() {
            initFields();
        }

        private Person(boolean noInit) {
        }

        private static final Person defaultInstance;

        public static Person getDefaultInstance() {
            return defaultInstance;
        }

        public Person getDefaultInstanceForType() {
            return defaultInstance;
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return com.dyuproject.protostuff.model.V2Speed.internal_static_testmodel_Person_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return com.dyuproject.protostuff.model.V2Speed.internal_static_testmodel_Person_fieldAccessorTable;
        }

        public static final int ID_FIELD_NUMBER = 1;

        private boolean hasId;

        private int id_ = 0;

        public boolean hasId() {
            return hasId;
        }

        public int getId() {
            return id_;
        }

        public static final int EMAIL_FIELD_NUMBER = 2;

        private boolean hasEmail;

        private java.lang.String email_ = "";

        public boolean hasEmail() {
            return hasEmail;
        }

        public java.lang.String getEmail() {
            return email_;
        }

        public static final int FIRST_NAME_FIELD_NUMBER = 3;

        private boolean hasFirstName;

        private java.lang.String firstName_ = "";

        public boolean hasFirstName() {
            return hasFirstName;
        }

        public java.lang.String getFirstName() {
            return firstName_;
        }

        public static final int LASTNAME_FIELD_NUMBER = 4;

        private boolean hasLastName;

        private java.lang.String lastName_ = "";

        public boolean hasLastName() {
            return hasLastName;
        }

        public java.lang.String getLastName() {
            return lastName_;
        }

        public static final int _DELEGATED_TASK__FIELD_NUMBER = 5;

        private java.util.List<com.dyuproject.protostuff.model.V2Speed.Task> DelegatedTask_ = java.util.Collections.emptyList();

        public java.util.List<com.dyuproject.protostuff.model.V2Speed.Task> getDelegatedTaskList() {
            return DelegatedTask_;
        }

        public int getDelegatedTaskCount() {
            return DelegatedTask_.size();
        }

        public com.dyuproject.protostuff.model.V2Speed.Task getDelegatedTask(int index) {
            return DelegatedTask_.get(index);
        }

        public static final int _PRIORITYTASK__FIELD_NUMBER = 6;

        private java.util.List<com.dyuproject.protostuff.model.V2Speed.Task> PriorityTask_ = java.util.Collections.emptyList();

        public java.util.List<com.dyuproject.protostuff.model.V2Speed.Task> getPriorityTaskList() {
            return PriorityTask_;
        }

        public int getPriorityTaskCount() {
            return PriorityTask_.size();
        }

        public com.dyuproject.protostuff.model.V2Speed.Task getPriorityTask(int index) {
            return PriorityTask_.get(index);
        }

        public static final int _AGE__FIELD_NUMBER = 7;

        private boolean hasAGe;

        private int AGe_ = 0;

        public boolean hasAGe() {
            return hasAGe;
        }

        public int getAGe() {
            return AGe_;
        }

        public static final int CURRENTTASK_FIELD_NUMBER = 8;

        private boolean hasCurrentTask;

        private com.dyuproject.protostuff.model.V2Speed.Task currentTask_;

        public boolean hasCurrentTask() {
            return hasCurrentTask;
        }

        public com.dyuproject.protostuff.model.V2Speed.Task getCurrentTask() {
            return currentTask_;
        }

        public static final int REPEATEDLONG_FIELD_NUMBER = 9;

        private java.util.List<java.lang.Long> repeatedLong_ = java.util.Collections.emptyList();

        public java.util.List<java.lang.Long> getRepeatedLongList() {
            return repeatedLong_;
        }

        public int getRepeatedLongCount() {
            return repeatedLong_.size();
        }

        public long getRepeatedLong(int index) {
            return repeatedLong_.get(index);
        }

        public static final int IMAGE_FIELD_NUMBER = 10;

        private java.util.List<com.google.protobuf.ByteString> image_ = java.util.Collections.emptyList();

        public java.util.List<com.google.protobuf.ByteString> getImageList() {
            return image_;
        }

        public int getImageCount() {
            return image_.size();
        }

        public com.google.protobuf.ByteString getImage(int index) {
            return image_.get(index);
        }

        private void initFields() {
            currentTask_ = com.dyuproject.protostuff.model.V2Speed.Task.getDefaultInstance();
        }

        public final boolean isInitialized() {
            if (!hasId) return false;
            for (com.dyuproject.protostuff.model.V2Speed.Task element : getDelegatedTaskList()) {
                if (!element.isInitialized()) return false;
            }
            for (com.dyuproject.protostuff.model.V2Speed.Task element : getPriorityTaskList()) {
                if (!element.isInitialized()) return false;
            }
            if (hasCurrentTask()) {
                if (!getCurrentTask().isInitialized()) return false;
            }
            return true;
        }

        public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
            getSerializedSize();
            if (hasId()) {
                output.writeInt32(1, getId());
            }
            if (hasEmail()) {
                output.writeString(2, getEmail());
            }
            if (hasFirstName()) {
                output.writeString(3, getFirstName());
            }
            if (hasLastName()) {
                output.writeString(4, getLastName());
            }
            for (com.dyuproject.protostuff.model.V2Speed.Task element : getDelegatedTaskList()) {
                output.writeMessage(5, element);
            }
            for (com.dyuproject.protostuff.model.V2Speed.Task element : getPriorityTaskList()) {
                output.writeMessage(6, element);
            }
            if (hasAGe()) {
                output.writeInt32(7, getAGe());
            }
            if (hasCurrentTask()) {
                output.writeMessage(8, getCurrentTask());
            }
            for (long element : getRepeatedLongList()) {
                output.writeInt64(9, element);
            }
            for (com.google.protobuf.ByteString element : getImageList()) {
                output.writeBytes(10, element);
            }
            getUnknownFields().writeTo(output);
        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;
            size = 0;
            if (hasId()) {
                size += com.google.protobuf.CodedOutputStream.computeInt32Size(1, getId());
            }
            if (hasEmail()) {
                size += com.google.protobuf.CodedOutputStream.computeStringSize(2, getEmail());
            }
            if (hasFirstName()) {
                size += com.google.protobuf.CodedOutputStream.computeStringSize(3, getFirstName());
            }
            if (hasLastName()) {
                size += com.google.protobuf.CodedOutputStream.computeStringSize(4, getLastName());
            }
            for (com.dyuproject.protostuff.model.V2Speed.Task element : getDelegatedTaskList()) {
                size += com.google.protobuf.CodedOutputStream.computeMessageSize(5, element);
            }
            for (com.dyuproject.protostuff.model.V2Speed.Task element : getPriorityTaskList()) {
                size += com.google.protobuf.CodedOutputStream.computeMessageSize(6, element);
            }
            if (hasAGe()) {
                size += com.google.protobuf.CodedOutputStream.computeInt32Size(7, getAGe());
            }
            if (hasCurrentTask()) {
                size += com.google.protobuf.CodedOutputStream.computeMessageSize(8, getCurrentTask());
            }
            {
                int dataSize = 0;
                for (long element : getRepeatedLongList()) {
                    dataSize += com.google.protobuf.CodedOutputStream.computeInt64SizeNoTag(element);
                }
                size += dataSize;
                size += 1 * getRepeatedLongList().size();
            }
            {
                int dataSize = 0;
                for (com.google.protobuf.ByteString element : getImageList()) {
                    dataSize += com.google.protobuf.CodedOutputStream.computeBytesSizeNoTag(element);
                }
                size += dataSize;
                size += 1 * getImageList().size();
            }
            size += getUnknownFields().getSerializedSize();
            memoizedSerializedSize = size;
            return size;
        }

        public static com.dyuproject.protostuff.model.V2Speed.Person parseFrom(com.google.protobuf.ByteString data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.dyuproject.protostuff.model.V2Speed.Person parseFrom(com.google.protobuf.ByteString data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.dyuproject.protostuff.model.V2Speed.Person parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.dyuproject.protostuff.model.V2Speed.Person parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.dyuproject.protostuff.model.V2Speed.Person parseFrom(java.io.InputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.dyuproject.protostuff.model.V2Speed.Person parseFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static com.dyuproject.protostuff.model.V2Speed.Person parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.dyuproject.protostuff.model.V2Speed.Person parseDelimitedFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.dyuproject.protostuff.model.V2Speed.Person parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.dyuproject.protostuff.model.V2Speed.Person parseFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(com.dyuproject.protostuff.model.V2Speed.Person prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> {

            private com.dyuproject.protostuff.model.V2Speed.Person result;

            private Builder() {
            }

            private static Builder create() {
                Builder builder = new Builder();
                builder.result = new com.dyuproject.protostuff.model.V2Speed.Person();
                return builder;
            }

            protected com.dyuproject.protostuff.model.V2Speed.Person internalGetResult() {
                return result;
            }

            public Builder clear() {
                if (result == null) {
                    throw new IllegalStateException("Cannot call clear() after build().");
                }
                result = new com.dyuproject.protostuff.model.V2Speed.Person();
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(result);
            }

            public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                return com.dyuproject.protostuff.model.V2Speed.Person.getDescriptor();
            }

            public com.dyuproject.protostuff.model.V2Speed.Person getDefaultInstanceForType() {
                return com.dyuproject.protostuff.model.V2Speed.Person.getDefaultInstance();
            }

            public boolean isInitialized() {
                return result.isInitialized();
            }

            public com.dyuproject.protostuff.model.V2Speed.Person build() {
                if (result != null && !isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return buildPartial();
            }

            private com.dyuproject.protostuff.model.V2Speed.Person buildParsed() throws com.google.protobuf.InvalidProtocolBufferException {
                if (!isInitialized()) {
                    throw newUninitializedMessageException(result).asInvalidProtocolBufferException();
                }
                return buildPartial();
            }

            public com.dyuproject.protostuff.model.V2Speed.Person buildPartial() {
                if (result == null) {
                    throw new IllegalStateException("build() has already been called on this Builder.");
                }
                if (result.DelegatedTask_ != java.util.Collections.EMPTY_LIST) {
                    result.DelegatedTask_ = java.util.Collections.unmodifiableList(result.DelegatedTask_);
                }
                if (result.PriorityTask_ != java.util.Collections.EMPTY_LIST) {
                    result.PriorityTask_ = java.util.Collections.unmodifiableList(result.PriorityTask_);
                }
                if (result.repeatedLong_ != java.util.Collections.EMPTY_LIST) {
                    result.repeatedLong_ = java.util.Collections.unmodifiableList(result.repeatedLong_);
                }
                if (result.image_ != java.util.Collections.EMPTY_LIST) {
                    result.image_ = java.util.Collections.unmodifiableList(result.image_);
                }
                com.dyuproject.protostuff.model.V2Speed.Person returnMe = result;
                result = null;
                return returnMe;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof com.dyuproject.protostuff.model.V2Speed.Person) {
                    return mergeFrom((com.dyuproject.protostuff.model.V2Speed.Person) other);
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(com.dyuproject.protostuff.model.V2Speed.Person other) {
                if (other == com.dyuproject.protostuff.model.V2Speed.Person.getDefaultInstance()) return this;
                if (other.hasId()) {
                    setId(other.getId());
                }
                if (other.hasEmail()) {
                    setEmail(other.getEmail());
                }
                if (other.hasFirstName()) {
                    setFirstName(other.getFirstName());
                }
                if (other.hasLastName()) {
                    setLastName(other.getLastName());
                }
                if (!other.DelegatedTask_.isEmpty()) {
                    if (result.DelegatedTask_.isEmpty()) {
                        result.DelegatedTask_ = new java.util.ArrayList<com.dyuproject.protostuff.model.V2Speed.Task>();
                    }
                    result.DelegatedTask_.addAll(other.DelegatedTask_);
                }
                if (!other.PriorityTask_.isEmpty()) {
                    if (result.PriorityTask_.isEmpty()) {
                        result.PriorityTask_ = new java.util.ArrayList<com.dyuproject.protostuff.model.V2Speed.Task>();
                    }
                    result.PriorityTask_.addAll(other.PriorityTask_);
                }
                if (other.hasAGe()) {
                    setAGe(other.getAGe());
                }
                if (other.hasCurrentTask()) {
                    mergeCurrentTask(other.getCurrentTask());
                }
                if (!other.repeatedLong_.isEmpty()) {
                    if (result.repeatedLong_.isEmpty()) {
                        result.repeatedLong_ = new java.util.ArrayList<java.lang.Long>();
                    }
                    result.repeatedLong_.addAll(other.repeatedLong_);
                }
                if (!other.image_.isEmpty()) {
                    if (result.image_.isEmpty()) {
                        result.image_ = new java.util.ArrayList<com.google.protobuf.ByteString>();
                    }
                    result.image_.addAll(other.image_);
                }
                this.mergeUnknownFields(other.getUnknownFields());
                return this;
            }

            public Builder mergeFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
                com.google.protobuf.UnknownFieldSet.Builder unknownFields = com.google.protobuf.UnknownFieldSet.newBuilder(this.getUnknownFields());
                while (true) {
                    int tag = input.readTag();
                    switch(tag) {
                        case 0:
                            this.setUnknownFields(unknownFields.build());
                            return this;
                        default:
                            {
                                if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                    this.setUnknownFields(unknownFields.build());
                                    return this;
                                }
                                break;
                            }
                        case 8:
                            {
                                setId(input.readInt32());
                                break;
                            }
                        case 18:
                            {
                                setEmail(input.readString());
                                break;
                            }
                        case 26:
                            {
                                setFirstName(input.readString());
                                break;
                            }
                        case 34:
                            {
                                setLastName(input.readString());
                                break;
                            }
                        case 42:
                            {
                                com.dyuproject.protostuff.model.V2Speed.Task.Builder subBuilder = com.dyuproject.protostuff.model.V2Speed.Task.newBuilder();
                                input.readMessage(subBuilder, extensionRegistry);
                                addDelegatedTask(subBuilder.buildPartial());
                                break;
                            }
                        case 50:
                            {
                                com.dyuproject.protostuff.model.V2Speed.Task.Builder subBuilder = com.dyuproject.protostuff.model.V2Speed.Task.newBuilder();
                                input.readMessage(subBuilder, extensionRegistry);
                                addPriorityTask(subBuilder.buildPartial());
                                break;
                            }
                        case 56:
                            {
                                setAGe(input.readInt32());
                                break;
                            }
                        case 66:
                            {
                                com.dyuproject.protostuff.model.V2Speed.Task.Builder subBuilder = com.dyuproject.protostuff.model.V2Speed.Task.newBuilder();
                                if (hasCurrentTask()) {
                                    subBuilder.mergeFrom(getCurrentTask());
                                }
                                input.readMessage(subBuilder, extensionRegistry);
                                setCurrentTask(subBuilder.buildPartial());
                                break;
                            }
                        case 72:
                            {
                                addRepeatedLong(input.readInt64());
                                break;
                            }
                        case 74:
                            {
                                int length = input.readRawVarint32();
                                int limit = input.pushLimit(length);
                                while (input.getBytesUntilLimit() > 0) {
                                    addRepeatedLong(input.readInt64());
                                }
                                input.popLimit(limit);
                                break;
                            }
                        case 82:
                            {
                                addImage(input.readBytes());
                                break;
                            }
                    }
                }
            }

            public boolean hasId() {
                return result.hasId();
            }

            public int getId() {
                return result.getId();
            }

            public Builder setId(int value) {
                result.hasId = true;
                result.id_ = value;
                return this;
            }

            public Builder clearId() {
                result.hasId = false;
                result.id_ = 0;
                return this;
            }

            public boolean hasEmail() {
                return result.hasEmail();
            }

            public java.lang.String getEmail() {
                return result.getEmail();
            }

            public Builder setEmail(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasEmail = true;
                result.email_ = value;
                return this;
            }

            public Builder clearEmail() {
                result.hasEmail = false;
                result.email_ = getDefaultInstance().getEmail();
                return this;
            }

            public boolean hasFirstName() {
                return result.hasFirstName();
            }

            public java.lang.String getFirstName() {
                return result.getFirstName();
            }

            public Builder setFirstName(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasFirstName = true;
                result.firstName_ = value;
                return this;
            }

            public Builder clearFirstName() {
                result.hasFirstName = false;
                result.firstName_ = getDefaultInstance().getFirstName();
                return this;
            }

            public boolean hasLastName() {
                return result.hasLastName();
            }

            public java.lang.String getLastName() {
                return result.getLastName();
            }

            public Builder setLastName(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasLastName = true;
                result.lastName_ = value;
                return this;
            }

            public Builder clearLastName() {
                result.hasLastName = false;
                result.lastName_ = getDefaultInstance().getLastName();
                return this;
            }

            public java.util.List<com.dyuproject.protostuff.model.V2Speed.Task> getDelegatedTaskList() {
                return java.util.Collections.unmodifiableList(result.DelegatedTask_);
            }

            public int getDelegatedTaskCount() {
                return result.getDelegatedTaskCount();
            }

            public com.dyuproject.protostuff.model.V2Speed.Task getDelegatedTask(int index) {
                return result.getDelegatedTask(index);
            }

            public Builder setDelegatedTask(int index, com.dyuproject.protostuff.model.V2Speed.Task value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.DelegatedTask_.set(index, value);
                return this;
            }

            public Builder setDelegatedTask(int index, com.dyuproject.protostuff.model.V2Speed.Task.Builder builderForValue) {
                result.DelegatedTask_.set(index, builderForValue.build());
                return this;
            }

            public Builder addDelegatedTask(com.dyuproject.protostuff.model.V2Speed.Task value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                if (result.DelegatedTask_.isEmpty()) {
                    result.DelegatedTask_ = new java.util.ArrayList<com.dyuproject.protostuff.model.V2Speed.Task>();
                }
                result.DelegatedTask_.add(value);
                return this;
            }

            public Builder addDelegatedTask(com.dyuproject.protostuff.model.V2Speed.Task.Builder builderForValue) {
                if (result.DelegatedTask_.isEmpty()) {
                    result.DelegatedTask_ = new java.util.ArrayList<com.dyuproject.protostuff.model.V2Speed.Task>();
                }
                result.DelegatedTask_.add(builderForValue.build());
                return this;
            }

            public Builder addAllDelegatedTask(java.lang.Iterable<? extends com.dyuproject.protostuff.model.V2Speed.Task> values) {
                if (result.DelegatedTask_.isEmpty()) {
                    result.DelegatedTask_ = new java.util.ArrayList<com.dyuproject.protostuff.model.V2Speed.Task>();
                }
                super.addAll(values, result.DelegatedTask_);
                return this;
            }

            public Builder clearDelegatedTask() {
                result.DelegatedTask_ = java.util.Collections.emptyList();
                return this;
            }

            public java.util.List<com.dyuproject.protostuff.model.V2Speed.Task> getPriorityTaskList() {
                return java.util.Collections.unmodifiableList(result.PriorityTask_);
            }

            public int getPriorityTaskCount() {
                return result.getPriorityTaskCount();
            }

            public com.dyuproject.protostuff.model.V2Speed.Task getPriorityTask(int index) {
                return result.getPriorityTask(index);
            }

            public Builder setPriorityTask(int index, com.dyuproject.protostuff.model.V2Speed.Task value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.PriorityTask_.set(index, value);
                return this;
            }

            public Builder setPriorityTask(int index, com.dyuproject.protostuff.model.V2Speed.Task.Builder builderForValue) {
                result.PriorityTask_.set(index, builderForValue.build());
                return this;
            }

            public Builder addPriorityTask(com.dyuproject.protostuff.model.V2Speed.Task value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                if (result.PriorityTask_.isEmpty()) {
                    result.PriorityTask_ = new java.util.ArrayList<com.dyuproject.protostuff.model.V2Speed.Task>();
                }
                result.PriorityTask_.add(value);
                return this;
            }

            public Builder addPriorityTask(com.dyuproject.protostuff.model.V2Speed.Task.Builder builderForValue) {
                if (result.PriorityTask_.isEmpty()) {
                    result.PriorityTask_ = new java.util.ArrayList<com.dyuproject.protostuff.model.V2Speed.Task>();
                }
                result.PriorityTask_.add(builderForValue.build());
                return this;
            }

            public Builder addAllPriorityTask(java.lang.Iterable<? extends com.dyuproject.protostuff.model.V2Speed.Task> values) {
                if (result.PriorityTask_.isEmpty()) {
                    result.PriorityTask_ = new java.util.ArrayList<com.dyuproject.protostuff.model.V2Speed.Task>();
                }
                super.addAll(values, result.PriorityTask_);
                return this;
            }

            public Builder clearPriorityTask() {
                result.PriorityTask_ = java.util.Collections.emptyList();
                return this;
            }

            public boolean hasAGe() {
                return result.hasAGe();
            }

            public int getAGe() {
                return result.getAGe();
            }

            public Builder setAGe(int value) {
                result.hasAGe = true;
                result.AGe_ = value;
                return this;
            }

            public Builder clearAGe() {
                result.hasAGe = false;
                result.AGe_ = 0;
                return this;
            }

            public boolean hasCurrentTask() {
                return result.hasCurrentTask();
            }

            public com.dyuproject.protostuff.model.V2Speed.Task getCurrentTask() {
                return result.getCurrentTask();
            }

            public Builder setCurrentTask(com.dyuproject.protostuff.model.V2Speed.Task value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasCurrentTask = true;
                result.currentTask_ = value;
                return this;
            }

            public Builder setCurrentTask(com.dyuproject.protostuff.model.V2Speed.Task.Builder builderForValue) {
                result.hasCurrentTask = true;
                result.currentTask_ = builderForValue.build();
                return this;
            }

            public Builder mergeCurrentTask(com.dyuproject.protostuff.model.V2Speed.Task value) {
                if (result.hasCurrentTask() && result.currentTask_ != com.dyuproject.protostuff.model.V2Speed.Task.getDefaultInstance()) {
                    result.currentTask_ = com.dyuproject.protostuff.model.V2Speed.Task.newBuilder(result.currentTask_).mergeFrom(value).buildPartial();
                } else {
                    result.currentTask_ = value;
                }
                result.hasCurrentTask = true;
                return this;
            }

            public Builder clearCurrentTask() {
                result.hasCurrentTask = false;
                result.currentTask_ = com.dyuproject.protostuff.model.V2Speed.Task.getDefaultInstance();
                return this;
            }

            public java.util.List<java.lang.Long> getRepeatedLongList() {
                return java.util.Collections.unmodifiableList(result.repeatedLong_);
            }

            public int getRepeatedLongCount() {
                return result.getRepeatedLongCount();
            }

            public long getRepeatedLong(int index) {
                return result.getRepeatedLong(index);
            }

            public Builder setRepeatedLong(int index, long value) {
                result.repeatedLong_.set(index, value);
                return this;
            }

            public Builder addRepeatedLong(long value) {
                if (result.repeatedLong_.isEmpty()) {
                    result.repeatedLong_ = new java.util.ArrayList<java.lang.Long>();
                }
                result.repeatedLong_.add(value);
                return this;
            }

            public Builder addAllRepeatedLong(java.lang.Iterable<? extends java.lang.Long> values) {
                if (result.repeatedLong_.isEmpty()) {
                    result.repeatedLong_ = new java.util.ArrayList<java.lang.Long>();
                }
                super.addAll(values, result.repeatedLong_);
                return this;
            }

            public Builder clearRepeatedLong() {
                result.repeatedLong_ = java.util.Collections.emptyList();
                return this;
            }

            public java.util.List<com.google.protobuf.ByteString> getImageList() {
                return java.util.Collections.unmodifiableList(result.image_);
            }

            public int getImageCount() {
                return result.getImageCount();
            }

            public com.google.protobuf.ByteString getImage(int index) {
                return result.getImage(index);
            }

            public Builder setImage(int index, com.google.protobuf.ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.image_.set(index, value);
                return this;
            }

            public Builder addImage(com.google.protobuf.ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                if (result.image_.isEmpty()) {
                    result.image_ = new java.util.ArrayList<com.google.protobuf.ByteString>();
                }
                result.image_.add(value);
                return this;
            }

            public Builder addAllImage(java.lang.Iterable<? extends com.google.protobuf.ByteString> values) {
                if (result.image_.isEmpty()) {
                    result.image_ = new java.util.ArrayList<com.google.protobuf.ByteString>();
                }
                super.addAll(values, result.image_);
                return this;
            }

            public Builder clearImage() {
                result.image_ = java.util.Collections.emptyList();
                return this;
            }
        }

        static {
            defaultInstance = new Person(true);
            com.dyuproject.protostuff.model.V2Speed.internalForceInit();
            defaultInstance.initFields();
        }
    }

    public static final class Task extends com.google.protobuf.GeneratedMessage {

        private Task() {
            initFields();
        }

        private Task(boolean noInit) {
        }

        private static final Task defaultInstance;

        public static Task getDefaultInstance() {
            return defaultInstance;
        }

        public Task getDefaultInstanceForType() {
            return defaultInstance;
        }

        public static final com.google.protobuf.Descriptors.Descriptor getDescriptor() {
            return com.dyuproject.protostuff.model.V2Speed.internal_static_testmodel_Task_descriptor;
        }

        protected com.google.protobuf.GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return com.dyuproject.protostuff.model.V2Speed.internal_static_testmodel_Task_fieldAccessorTable;
        }

        public enum Status implements com.google.protobuf.ProtocolMessageEnum {

            PENDING(0, 0), STARTED(1, 1), COMPLETED(2, 2);

            public final int getNumber() {
                return value;
            }

            public static Status valueOf(int value) {
                switch(value) {
                    case 0:
                        return PENDING;
                    case 1:
                        return STARTED;
                    case 2:
                        return COMPLETED;
                    default:
                        return null;
                }
            }

            public static com.google.protobuf.Internal.EnumLiteMap<Status> internalGetValueMap() {
                return internalValueMap;
            }

            private static com.google.protobuf.Internal.EnumLiteMap<Status> internalValueMap = new com.google.protobuf.Internal.EnumLiteMap<Status>() {

                public Status findValueByNumber(int number) {
                    return Status.valueOf(number);
                }
            };

            public final com.google.protobuf.Descriptors.EnumValueDescriptor getValueDescriptor() {
                return getDescriptor().getValues().get(index);
            }

            public final com.google.protobuf.Descriptors.EnumDescriptor getDescriptorForType() {
                return getDescriptor();
            }

            public static final com.google.protobuf.Descriptors.EnumDescriptor getDescriptor() {
                return com.dyuproject.protostuff.model.V2Speed.Task.getDescriptor().getEnumTypes().get(0);
            }

            private static final Status[] VALUES = { PENDING, STARTED, COMPLETED };

            public static Status valueOf(com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
                if (desc.getType() != getDescriptor()) {
                    throw new java.lang.IllegalArgumentException("EnumValueDescriptor is not for this type.");
                }
                return VALUES[desc.getIndex()];
            }

            private final int index;

            private final int value;

            private Status(int index, int value) {
                this.index = index;
                this.value = value;
            }

            static {
                com.dyuproject.protostuff.model.V2Speed.getDescriptor();
            }
        }

        public static final int _ID_FIELD_NUMBER = 1;

        private boolean hasId;

        private int Id_ = 0;

        public boolean hasId() {
            return hasId;
        }

        public int getId() {
            return Id_;
        }

        public static final int NAME__FIELD_NUMBER = 2;

        private boolean hasName;

        private java.lang.String name_ = "";

        public boolean hasName() {
            return hasName;
        }

        public java.lang.String getName() {
            return name_;
        }

        public static final int _DESCRIPTION__FIELD_NUMBER = 3;

        private boolean hasDescription;

        private java.lang.String Description_ = "";

        public boolean hasDescription() {
            return hasDescription;
        }

        public java.lang.String getDescription() {
            return Description_;
        }

        public static final int STATUS_FIELD_NUMBER = 4;

        private boolean hasStatus;

        private com.dyuproject.protostuff.model.V2Speed.Task.Status status_;

        public boolean hasStatus() {
            return hasStatus;
        }

        public com.dyuproject.protostuff.model.V2Speed.Task.Status getStatus() {
            return status_;
        }

        public static final int ATTACHMENT_FIELD_NUMBER = 5;

        private boolean hasAttachment;

        private com.google.protobuf.ByteString attachment_ = com.google.protobuf.ByteString.EMPTY;

        public boolean hasAttachment() {
            return hasAttachment;
        }

        public com.google.protobuf.ByteString getAttachment() {
            return attachment_;
        }

        public static final int ABOOLEAN_FIELD_NUMBER = 6;

        private boolean hasAboolean;

        private boolean aboolean_ = false;

        public boolean hasAboolean() {
            return hasAboolean;
        }

        public boolean getAboolean() {
            return aboolean_;
        }

        public static final int AFLOAT_FIELD_NUMBER = 7;

        private boolean hasAfloat;

        private float afloat_ = 0F;

        public boolean hasAfloat() {
            return hasAfloat;
        }

        public float getAfloat() {
            return afloat_;
        }

        public static final int ADOUBLE_FIELD_NUMBER = 8;

        private boolean hasAdouble;

        private double adouble_ = 0D;

        public boolean hasAdouble() {
            return hasAdouble;
        }

        public double getAdouble() {
            return adouble_;
        }

        public static final int ALONG_FIELD_NUMBER = 9;

        private boolean hasAlong;

        private long along_ = 0L;

        public boolean hasAlong() {
            return hasAlong;
        }

        public long getAlong() {
            return along_;
        }

        private void initFields() {
            status_ = com.dyuproject.protostuff.model.V2Speed.Task.Status.PENDING;
        }

        public final boolean isInitialized() {
            if (!hasId) return false;
            return true;
        }

        public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
            getSerializedSize();
            if (hasId()) {
                output.writeInt32(1, getId());
            }
            if (hasName()) {
                output.writeString(2, getName());
            }
            if (hasDescription()) {
                output.writeString(3, getDescription());
            }
            if (hasStatus()) {
                output.writeEnum(4, getStatus().getNumber());
            }
            if (hasAttachment()) {
                output.writeBytes(5, getAttachment());
            }
            if (hasAboolean()) {
                output.writeBool(6, getAboolean());
            }
            if (hasAfloat()) {
                output.writeFloat(7, getAfloat());
            }
            if (hasAdouble()) {
                output.writeDouble(8, getAdouble());
            }
            if (hasAlong()) {
                output.writeInt64(9, getAlong());
            }
            getUnknownFields().writeTo(output);
        }

        private int memoizedSerializedSize = -1;

        public int getSerializedSize() {
            int size = memoizedSerializedSize;
            if (size != -1) return size;
            size = 0;
            if (hasId()) {
                size += com.google.protobuf.CodedOutputStream.computeInt32Size(1, getId());
            }
            if (hasName()) {
                size += com.google.protobuf.CodedOutputStream.computeStringSize(2, getName());
            }
            if (hasDescription()) {
                size += com.google.protobuf.CodedOutputStream.computeStringSize(3, getDescription());
            }
            if (hasStatus()) {
                size += com.google.protobuf.CodedOutputStream.computeEnumSize(4, getStatus().getNumber());
            }
            if (hasAttachment()) {
                size += com.google.protobuf.CodedOutputStream.computeBytesSize(5, getAttachment());
            }
            if (hasAboolean()) {
                size += com.google.protobuf.CodedOutputStream.computeBoolSize(6, getAboolean());
            }
            if (hasAfloat()) {
                size += com.google.protobuf.CodedOutputStream.computeFloatSize(7, getAfloat());
            }
            if (hasAdouble()) {
                size += com.google.protobuf.CodedOutputStream.computeDoubleSize(8, getAdouble());
            }
            if (hasAlong()) {
                size += com.google.protobuf.CodedOutputStream.computeInt64Size(9, getAlong());
            }
            size += getUnknownFields().getSerializedSize();
            memoizedSerializedSize = size;
            return size;
        }

        public static com.dyuproject.protostuff.model.V2Speed.Task parseFrom(com.google.protobuf.ByteString data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.dyuproject.protostuff.model.V2Speed.Task parseFrom(com.google.protobuf.ByteString data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.dyuproject.protostuff.model.V2Speed.Task parseFrom(byte[] data) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data).buildParsed();
        }

        public static com.dyuproject.protostuff.model.V2Speed.Task parseFrom(byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws com.google.protobuf.InvalidProtocolBufferException {
            return newBuilder().mergeFrom(data, extensionRegistry).buildParsed();
        }

        public static com.dyuproject.protostuff.model.V2Speed.Task parseFrom(java.io.InputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.dyuproject.protostuff.model.V2Speed.Task parseFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static com.dyuproject.protostuff.model.V2Speed.Task parseDelimitedFrom(java.io.InputStream input) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.dyuproject.protostuff.model.V2Speed.Task parseDelimitedFrom(java.io.InputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            Builder builder = newBuilder();
            if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
                return builder.buildParsed();
            } else {
                return null;
            }
        }

        public static com.dyuproject.protostuff.model.V2Speed.Task parseFrom(com.google.protobuf.CodedInputStream input) throws java.io.IOException {
            return newBuilder().mergeFrom(input).buildParsed();
        }

        public static com.dyuproject.protostuff.model.V2Speed.Task parseFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
            return newBuilder().mergeFrom(input, extensionRegistry).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(com.dyuproject.protostuff.model.V2Speed.Task prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        public static final class Builder extends com.google.protobuf.GeneratedMessage.Builder<Builder> {

            private com.dyuproject.protostuff.model.V2Speed.Task result;

            private Builder() {
            }

            private static Builder create() {
                Builder builder = new Builder();
                builder.result = new com.dyuproject.protostuff.model.V2Speed.Task();
                return builder;
            }

            protected com.dyuproject.protostuff.model.V2Speed.Task internalGetResult() {
                return result;
            }

            public Builder clear() {
                if (result == null) {
                    throw new IllegalStateException("Cannot call clear() after build().");
                }
                result = new com.dyuproject.protostuff.model.V2Speed.Task();
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(result);
            }

            public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() {
                return com.dyuproject.protostuff.model.V2Speed.Task.getDescriptor();
            }

            public com.dyuproject.protostuff.model.V2Speed.Task getDefaultInstanceForType() {
                return com.dyuproject.protostuff.model.V2Speed.Task.getDefaultInstance();
            }

            public boolean isInitialized() {
                return result.isInitialized();
            }

            public com.dyuproject.protostuff.model.V2Speed.Task build() {
                if (result != null && !isInitialized()) {
                    throw newUninitializedMessageException(result);
                }
                return buildPartial();
            }

            private com.dyuproject.protostuff.model.V2Speed.Task buildParsed() throws com.google.protobuf.InvalidProtocolBufferException {
                if (!isInitialized()) {
                    throw newUninitializedMessageException(result).asInvalidProtocolBufferException();
                }
                return buildPartial();
            }

            public com.dyuproject.protostuff.model.V2Speed.Task buildPartial() {
                if (result == null) {
                    throw new IllegalStateException("build() has already been called on this Builder.");
                }
                com.dyuproject.protostuff.model.V2Speed.Task returnMe = result;
                result = null;
                return returnMe;
            }

            public Builder mergeFrom(com.google.protobuf.Message other) {
                if (other instanceof com.dyuproject.protostuff.model.V2Speed.Task) {
                    return mergeFrom((com.dyuproject.protostuff.model.V2Speed.Task) other);
                } else {
                    super.mergeFrom(other);
                    return this;
                }
            }

            public Builder mergeFrom(com.dyuproject.protostuff.model.V2Speed.Task other) {
                if (other == com.dyuproject.protostuff.model.V2Speed.Task.getDefaultInstance()) return this;
                if (other.hasId()) {
                    setId(other.getId());
                }
                if (other.hasName()) {
                    setName(other.getName());
                }
                if (other.hasDescription()) {
                    setDescription(other.getDescription());
                }
                if (other.hasStatus()) {
                    setStatus(other.getStatus());
                }
                if (other.hasAttachment()) {
                    setAttachment(other.getAttachment());
                }
                if (other.hasAboolean()) {
                    setAboolean(other.getAboolean());
                }
                if (other.hasAfloat()) {
                    setAfloat(other.getAfloat());
                }
                if (other.hasAdouble()) {
                    setAdouble(other.getAdouble());
                }
                if (other.hasAlong()) {
                    setAlong(other.getAlong());
                }
                this.mergeUnknownFields(other.getUnknownFields());
                return this;
            }

            public Builder mergeFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) throws java.io.IOException {
                com.google.protobuf.UnknownFieldSet.Builder unknownFields = com.google.protobuf.UnknownFieldSet.newBuilder(this.getUnknownFields());
                while (true) {
                    int tag = input.readTag();
                    switch(tag) {
                        case 0:
                            this.setUnknownFields(unknownFields.build());
                            return this;
                        default:
                            {
                                if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                    this.setUnknownFields(unknownFields.build());
                                    return this;
                                }
                                break;
                            }
                        case 8:
                            {
                                setId(input.readInt32());
                                break;
                            }
                        case 18:
                            {
                                setName(input.readString());
                                break;
                            }
                        case 26:
                            {
                                setDescription(input.readString());
                                break;
                            }
                        case 32:
                            {
                                int rawValue = input.readEnum();
                                com.dyuproject.protostuff.model.V2Speed.Task.Status value = com.dyuproject.protostuff.model.V2Speed.Task.Status.valueOf(rawValue);
                                if (value == null) {
                                    unknownFields.mergeVarintField(4, rawValue);
                                } else {
                                    setStatus(value);
                                }
                                break;
                            }
                        case 42:
                            {
                                setAttachment(input.readBytes());
                                break;
                            }
                        case 48:
                            {
                                setAboolean(input.readBool());
                                break;
                            }
                        case 61:
                            {
                                setAfloat(input.readFloat());
                                break;
                            }
                        case 65:
                            {
                                setAdouble(input.readDouble());
                                break;
                            }
                        case 72:
                            {
                                setAlong(input.readInt64());
                                break;
                            }
                    }
                }
            }

            public boolean hasId() {
                return result.hasId();
            }

            public int getId() {
                return result.getId();
            }

            public Builder setId(int value) {
                result.hasId = true;
                result.Id_ = value;
                return this;
            }

            public Builder clearId() {
                result.hasId = false;
                result.Id_ = 0;
                return this;
            }

            public boolean hasName() {
                return result.hasName();
            }

            public java.lang.String getName() {
                return result.getName();
            }

            public Builder setName(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasName = true;
                result.name_ = value;
                return this;
            }

            public Builder clearName() {
                result.hasName = false;
                result.name_ = getDefaultInstance().getName();
                return this;
            }

            public boolean hasDescription() {
                return result.hasDescription();
            }

            public java.lang.String getDescription() {
                return result.getDescription();
            }

            public Builder setDescription(java.lang.String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasDescription = true;
                result.Description_ = value;
                return this;
            }

            public Builder clearDescription() {
                result.hasDescription = false;
                result.Description_ = getDefaultInstance().getDescription();
                return this;
            }

            public boolean hasStatus() {
                return result.hasStatus();
            }

            public com.dyuproject.protostuff.model.V2Speed.Task.Status getStatus() {
                return result.getStatus();
            }

            public Builder setStatus(com.dyuproject.protostuff.model.V2Speed.Task.Status value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasStatus = true;
                result.status_ = value;
                return this;
            }

            public Builder clearStatus() {
                result.hasStatus = false;
                result.status_ = com.dyuproject.protostuff.model.V2Speed.Task.Status.PENDING;
                return this;
            }

            public boolean hasAttachment() {
                return result.hasAttachment();
            }

            public com.google.protobuf.ByteString getAttachment() {
                return result.getAttachment();
            }

            public Builder setAttachment(com.google.protobuf.ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                result.hasAttachment = true;
                result.attachment_ = value;
                return this;
            }

            public Builder clearAttachment() {
                result.hasAttachment = false;
                result.attachment_ = getDefaultInstance().getAttachment();
                return this;
            }

            public boolean hasAboolean() {
                return result.hasAboolean();
            }

            public boolean getAboolean() {
                return result.getAboolean();
            }

            public Builder setAboolean(boolean value) {
                result.hasAboolean = true;
                result.aboolean_ = value;
                return this;
            }

            public Builder clearAboolean() {
                result.hasAboolean = false;
                result.aboolean_ = false;
                return this;
            }

            public boolean hasAfloat() {
                return result.hasAfloat();
            }

            public float getAfloat() {
                return result.getAfloat();
            }

            public Builder setAfloat(float value) {
                result.hasAfloat = true;
                result.afloat_ = value;
                return this;
            }

            public Builder clearAfloat() {
                result.hasAfloat = false;
                result.afloat_ = 0F;
                return this;
            }

            public boolean hasAdouble() {
                return result.hasAdouble();
            }

            public double getAdouble() {
                return result.getAdouble();
            }

            public Builder setAdouble(double value) {
                result.hasAdouble = true;
                result.adouble_ = value;
                return this;
            }

            public Builder clearAdouble() {
                result.hasAdouble = false;
                result.adouble_ = 0D;
                return this;
            }

            public boolean hasAlong() {
                return result.hasAlong();
            }

            public long getAlong() {
                return result.getAlong();
            }

            public Builder setAlong(long value) {
                result.hasAlong = true;
                result.along_ = value;
                return this;
            }

            public Builder clearAlong() {
                result.hasAlong = false;
                result.along_ = 0L;
                return this;
            }
        }

        static {
            defaultInstance = new Task(true);
            com.dyuproject.protostuff.model.V2Speed.internalForceInit();
            defaultInstance.initFields();
        }
    }

    private static com.google.protobuf.Descriptors.Descriptor internal_static_testmodel_Person_descriptor;

    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_testmodel_Person_fieldAccessorTable;

    private static com.google.protobuf.Descriptors.Descriptor internal_static_testmodel_Task_descriptor;

    private static com.google.protobuf.GeneratedMessage.FieldAccessorTable internal_static_testmodel_Task_fieldAccessorTable;

    public static com.google.protobuf.Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    private static com.google.protobuf.Descriptors.FileDescriptor descriptor;

    static {
        java.lang.String[] descriptorData = { "\n\"src/test/resources/TestModel.proto\022\tte" + "stmodel\"\367\001\n\006Person\022\n\n\002id\030\001 \002(\005\022\r\n\005email\030" + "\002 \001(\t\022\022\n\nfirst_name\030\003 \001(\t\022\020\n\010lastName\030\004 " + "\001(\t\022)\n\020_delegated_task_\030\005 \003(\0132\017.testmode" + "l.Task\022\'\n\016_priorityTask_\030\006 \003(\0132\017.testmod" + "el.Task\022\r\n\005_aGe_\030\007 \001(\005\022$\n\013currentTask\030\010 " + "\001(\0132\017.testmodel.Task\022\024\n\014repeatedLong\030\t \003" + "(\003\022\r\n\005image\030\n \003(\014\"\352\001\n\004Task\022\013\n\003_id\030\001 \002(\005\022" + "\r\n\005name_\030\002 \001(\t\022\025\n\r_description_\030\003 \001(\t\022&\n" + "\006status\030\004 \001(\0162\026.testmodel.Task.Status\022\022\n", "\nattachment\030\005 \001(\014\022\020\n\010aboolean\030\006 \001(\010\022\016\n\006a" + "float\030\007 \001(\002\022\017\n\007adouble\030\010 \001(\001\022\r\n\005along\030\t " + "\001(\003\"1\n\006Status\022\013\n\007PENDING\020\000\022\013\n\007STARTED\020\001\022" + "\r\n\tCOMPLETED\020\002B,\n\037com.dyuproject.protost" + "uff.modelB\007V2SpeedH\001" };
        com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {

            public com.google.protobuf.ExtensionRegistry assignDescriptors(com.google.protobuf.Descriptors.FileDescriptor root) {
                descriptor = root;
                internal_static_testmodel_Person_descriptor = getDescriptor().getMessageTypes().get(0);
                internal_static_testmodel_Person_fieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(internal_static_testmodel_Person_descriptor, new java.lang.String[] { "Id", "Email", "FirstName", "LastName", "DelegatedTask", "PriorityTask", "AGe", "CurrentTask", "RepeatedLong", "Image" }, com.dyuproject.protostuff.model.V2Speed.Person.class, com.dyuproject.protostuff.model.V2Speed.Person.Builder.class);
                internal_static_testmodel_Task_descriptor = getDescriptor().getMessageTypes().get(1);
                internal_static_testmodel_Task_fieldAccessorTable = new com.google.protobuf.GeneratedMessage.FieldAccessorTable(internal_static_testmodel_Task_descriptor, new java.lang.String[] { "Id", "Name", "Description", "Status", "Attachment", "Aboolean", "Afloat", "Adouble", "Along" }, com.dyuproject.protostuff.model.V2Speed.Task.class, com.dyuproject.protostuff.model.V2Speed.Task.Builder.class);
                return null;
            }
        };
        com.google.protobuf.Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new com.google.protobuf.Descriptors.FileDescriptor[] {}, assigner);
    }

    public static void internalForceInit() {
    }
}
