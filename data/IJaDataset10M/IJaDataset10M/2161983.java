package edu.rice.cs.plt.recur;

import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import edu.rice.cs.plt.tuple.Pair;
import edu.rice.cs.plt.tuple.IdentityPair;
import edu.rice.cs.plt.lambda.Thunk;
import edu.rice.cs.plt.lambda.Lambda2;
import edu.rice.cs.plt.lambda.LambdaUtil;

/**
 * <p>A stack used to store the arguments of a recursive invocation in order to prevent
 * infinite recursion.  By checking that the given arguments have not been used previously 
 * before recurring, a client can prevent infinite recursion in some circumstances (such as
 * when traversing an infinite, immutable data structure).</p>
 * 
 * <p>While {@link RecursionStack2} allows arbitrary application result values to be provided 
 * for the infinite case, this class follows a stricter discipline: the infinite case result
 * must be provided at the time of the <em>first</em> invocation of the arguments; that value
 * will be stored, and a second invocation will return it.  In this way, the result of
 * a recursive computation is always precomputed -- that is, it must be determined before 
 * the computation takes place.  Classes like {@link edu.rice.cs.plt.lambda.DelayedThunk} can be 
 * used to create precomputed values, providing an initial "empty box" that can be "filled" when 
 * computation is complete.  This allows the definition, for example, of data structures that 
 * contain themselves.  Due to the restricted applicability of this class (in comparison to
 * {@code RecursionStack2}), methods that involve invoking {@code Runnable}s or recurring multiple
 * times based on a threshold value are not defined here.</p>
 * 
 * <p>The client may either choose to explicity check for containment, {@link #push} the argument, 
 * recur, and then {@link #pop}, or invoke one of a variety of lambda-based methods that perform 
 * these bookkeeping tasks automatically.  In the latter case, when an exception occurs between
 * a {@code push} and a matching {@code pop}, the {@code pop} is guaranteed to execute before 
 * the exception propagates upward.  Thus, clients who do not directly invoke {@link #push}
 * and {@link #pop} may assume that the stack is always in a consistent state.</p>
 * 
 * @see RecursionStack2
 * @see PrecomputedRecursionStack
 * @see PrecomputedRecursionStack3
 * @see PrecomputedRecursionStack4
 */
public class PrecomputedRecursionStack2<T1, T2, R> {

    private final Lambda2<? super T1, ? super T2, ? extends Pair<T1, T2>> _pairFactory;

    private final Map<Pair<T1, T2>, Lambda2<? super T1, ? super T2, ? extends R>> _previous;

    private final LinkedList<Pair<T1, T2>> _stack;

    /** Create an empty recursion stack with an {@link IdentityPair} factory */
    public PrecomputedRecursionStack2() {
        this(IdentityPair.<T1, T2>factory());
    }

    /**
   * Create an empty recursion stack with the given {@code Pair} factory
   * @param pairFactory  A lambda used to produce a pair for values placed on the
   *                     stack.  This provides clients with control over the method used
   *                     to determine if a value has been seen previously.
   */
    public PrecomputedRecursionStack2(Lambda2<? super T1, ? super T2, ? extends Pair<T1, T2>> pairFactory) {
        _pairFactory = pairFactory;
        _previous = new HashMap<Pair<T1, T2>, Lambda2<? super T1, ? super T2, ? extends R>>();
        _stack = new LinkedList<Pair<T1, T2>>();
    }

    /** 
   * @return  {@code true} iff values identical (according to {@code ==}) to the given arguments
   *          are currently on the stack
   */
    public boolean contains(T1 arg1, T2 arg2) {
        return _previous.containsKey(_pairFactory.value(arg1, arg2));
    }

    /** 
   * @return  The infinite-case result provided for the given arguments
   * @throws  IllegalStateException  If the arguments are not on the stack
   */
    public R get(T1 arg1, T2 arg2) {
        Lambda2<? super T1, ? super T2, ? extends R> result = _previous.get(_pairFactory.value(arg1, arg2));
        if (result == null) {
            throw new IllegalArgumentException("Values are not on the stack");
        }
        return result.value(arg1, arg2);
    }

    /**
   * Add the given arguments to the top of the stack with the given infinite-case result.
   * @throws IllegalArgumentException  If the arguments are already on the stack
   */
    public void push(T1 arg1, T2 arg2, R value) {
        push(arg1, arg2, (Lambda2<Object, Object, R>) LambdaUtil.valueLambda(value));
    }

    /**
   * Add the given arguments to the top of the stack with the given thunk producing their 
   * infinite-case result.
   * @throws IllegalArgumentException  If the arguments are already on the stack
   */
    public void push(T1 arg1, T2 arg2, Thunk<? extends R> value) {
        push(arg1, arg2, (Lambda2<Object, Object, ? extends R>) LambdaUtil.promote(value));
    }

    /**
   * Add the given arguments to the top of the stack with the given lambda producing their 
   * infinite-case result.
   * @throws IllegalArgumentException  If the arguments are already on the stack
   */
    public void push(T1 arg1, T2 arg2, Lambda2<? super T1, ? super T2, ? extends R> value) {
        Pair<T1, T2> wrapped = _pairFactory.value(arg1, arg2);
        if (_previous.containsKey(wrapped)) {
            throw new IllegalArgumentException("The given arguments are already on the stack");
        }
        _stack.addLast(wrapped);
        _previous.put(wrapped, value);
    }

    /** 
   * Remove the given arguments from the top of the stack
   * @throws IllegalArgumentException  If the arguments are not at the top of the stack
   */
    public void pop(T1 arg1, T2 arg2) {
        Pair<T1, T2> wrapped = _pairFactory.value(arg1, arg2);
        if (_stack.isEmpty() || !_stack.getLast().equals(wrapped)) {
            throw new IllegalArgumentException("the given arguments are not on top of the stack");
        }
        _stack.removeLast();
        _previous.remove(wrapped);
    }

    /** @return  The current size (depth) of the stack */
    public int size() {
        return _stack.size();
    }

    /** @return  {@code true} iff the stack is currently empty */
    public boolean isEmpty() {
        return _stack.isEmpty();
    }

    /**
   * Evaluate the given thunk, unless the given arguments are already on the stack; push the
   * arguments onto the stack with the given precomputed result during {@code thunk}'s evaluation
   * 
   * @return  The value of {@code thunk}, or a previously-provided precomputed value
   */
    public R apply(Thunk<? extends R> thunk, R precomputed, T1 arg1, T2 arg2) {
        if (!contains(arg1, arg2)) {
            push(arg1, arg2, precomputed);
            try {
                return thunk.value();
            } finally {
                pop(arg1, arg2);
            }
        } else {
            return get(arg1, arg2);
        }
    }

    /**
   * Evaluate the given thunk, unless the given arguments are already on the stack; push the
   * arguments onto the stack with the given precomputed result during {@code thunk}'s evaluation
   * 
   * @return  The value of {@code thunk}, or a previously-provided precomputed value
   */
    public R apply(Thunk<? extends R> thunk, Thunk<? extends R> precomputed, T1 arg1, T2 arg2) {
        if (!contains(arg1, arg2)) {
            push(arg1, arg2, precomputed);
            try {
                return thunk.value();
            } finally {
                pop(arg1, arg2);
            }
        } else {
            return get(arg1, arg2);
        }
    }

    /**
   * Evaluate the given thunk, unless the given arguments are already on the stack; push the
   * arguments onto the stack with the given precomputed result during {@code thunk}'s evaluation
   * 
   * @return  The value of {@code thunk}, or a previously-provided precomputed value
   */
    public R apply(Thunk<? extends R> thunk, Lambda2<? super T1, ? super T2, ? extends R> precomputed, T1 arg1, T2 arg2) {
        if (!contains(arg1, arg2)) {
            push(arg1, arg2, precomputed);
            try {
                return thunk.value();
            } finally {
                pop(arg1, arg2);
            }
        } else {
            return get(arg1, arg2);
        }
    }

    /**
   * Evaluate the given lambda with the given arguments, unless the arguments are already on the 
   * stack; push the arguments onto the stack with the given precomputed result during 
   * {@code lambda}'s evaluation
   * 
   * @return  The value of {@code lambda}, or a previously-provided precomputed value
   */
    public R apply(Lambda2<? super T1, ? super T2, ? extends R> lambda, R precomputed, T1 arg1, T2 arg2) {
        if (!contains(arg1, arg2)) {
            push(arg1, arg2, precomputed);
            try {
                return lambda.value(arg1, arg2);
            } finally {
                pop(arg1, arg2);
            }
        } else {
            return get(arg1, arg2);
        }
    }

    /**
   * Evaluate the given lambda with the given arguments, unless the arguments are already on the 
   * stack; push the arguments onto the stack with the given precomputed result during 
   * {@code lambda}'s evaluation
   * 
   * @return  The value of {@code lambda}, or a previously-provided precomputed value
   */
    public R apply(Lambda2<? super T1, ? super T2, ? extends R> lambda, Thunk<? extends R> precomputed, T1 arg1, T2 arg2) {
        if (!contains(arg1, arg2)) {
            push(arg1, arg2, precomputed);
            try {
                return lambda.value(arg1, arg2);
            } finally {
                pop(arg1, arg2);
            }
        } else {
            return get(arg1, arg2);
        }
    }

    /**
   * Evaluate the given lambda with the given arguments, unless the arguments are already on the 
   * stack; push the arguments onto the stack with the given precomputed result during 
   * {@code lambda}'s evaluation
   * 
   * @return  The value of {@code lambda}, or a previously-provided precomputed value
   */
    public R apply(Lambda2<? super T1, ? super T2, ? extends R> lambda, Lambda2<? super T1, ? super T2, ? extends R> precomputed, T1 arg1, T2 arg2) {
        if (!contains(arg1, arg2)) {
            push(arg1, arg2, precomputed);
            try {
                return lambda.value(arg1, arg2);
            } finally {
                pop(arg1, arg2);
            }
        } else {
            return get(arg1, arg2);
        }
    }

    /** Call the constructor (allows the type arguments to be inferred) */
    public static <T1, T2, R> PrecomputedRecursionStack2<T1, T2, R> make() {
        return new PrecomputedRecursionStack2<T1, T2, R>();
    }

    /** Call the constructor (allows the type arguments to be inferred) */
    public static <T1, T2, R> PrecomputedRecursionStack2<T1, T2, R> make(Lambda2<? super T1, ? super T2, ? extends Pair<T1, T2>> pairFactory) {
        return new PrecomputedRecursionStack2<T1, T2, R>(pairFactory);
    }
}
