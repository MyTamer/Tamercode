package org.ujoframework.criterion;

import java.util.Collection;
import org.ujoframework.Ujo;
import org.ujoframework.UjoProperty;

/**
 * An abstract immutable rule provides a basic interface and static factory methods. You can use it:
 * <ul>
 *    <li>like a generic UJO object validator (2)</li>
 *    <li>to create a query on the UJO list (1)</li>
 *    <li>the class is used to build 'SQL query' in the module </strong>ujo-orm</strong> (sience 0.90)</li>
 * </ul>
 *
 * There is allowed to join two instances (based on the same BO) to a binary tree by a new Rule.
 * Some common operators (and, or, not) are implemeted into a special join method of the Criteron class.
 *
 * <h3>Example of use</h3>
 * <pre class="pre"><span class="comment">// Make a rule:</span>
 * Rule&lt;Person&gt; crn1, crn2, rule;
 * crn1 = Rule.where(CASH, Operator.GT, 10.0);
 * crn2 = Rule.where(CASH, Operator.LE, 20.0);
 * rule = crn1.and(crn2);
 *
 * <span class="comment">// Use a rule (1):</span>
 * CriteriaTool&lt;Person&gt; ct = CriteriaTool.where();
 * List&lt;Person&gt; result = ct.select(persons, rule);
 * assertEquals(1, result.size());
 * assertEquals(20.0, CASH.of(result.get(0)));
 *
 * <span class="comment">// Use a rule (2):</span>
 * Person person = result.get(0);
 * <span class="keyword-directive">boolean</span> validation = rule.evaluate(person);
 * assertTrue(validation);
 * </pre>
 *
 * <h3>Using the parentheses</h3>
 * A Rule instance composed from another rules works as an expression separated by parentheses.
 * See the next two examples:
 * <pre class="pre"><span class="comment">// Consider instances:</span>
 * Rule&lt;Person&gt; a, b, c, result;
 * a = Rule.where(CASH, Operator.GT, 10.0);
 * b = Rule.where(CASH, Operator.LE, 20.0);
 * c = Rule.where(NAME, Operator.STARTS, "P");
 *
 * <span class="comment">// Expression #1: (<span class="highlight">a OR b</span>) AND c :</span>
 * result = (<span class="highlight">a.or(b)</span>).and(c); <span class="comment">// or simply:</span>
 * result = <span class="highlight">a.or(b)</span>.and(c);

 * <span class="comment">// Expression #2: a AND (<span class="highlight">b OR c</span>) :</span>
 * result = a.and(<span class="highlight">b.or(c)</span>);
 * </pre>
 *
 * @since 0.90
 * @author Pavel Ponec
 * @composed 1 - 1 AbstractOperator
 */
public abstract class Rule<UJO extends Ujo> {

    public abstract boolean evaluate(UJO ujo);

    public Rule<UJO> join(BinaryOperator operator, Rule<UJO> criterion) {
        return new BinaryCriterion<UJO>(this, operator, criterion);
    }

    public Rule<UJO> and(Rule<UJO> criterion) {
        return join(BinaryOperator.AND, criterion);
    }

    public Rule<UJO> or(Rule<UJO> criterion) {
        return join(BinaryOperator.OR, criterion);
    }

    public Rule<UJO> not() {
        return new BinaryCriterion<UJO>(this, BinaryOperator.NOT, this);
    }

    /** Returns the left node of the parrent */
    public abstract Object getLeftNode();

    /** Returns the right node of the parrent */
    public abstract Object getRightNode();

    /** Returns an operator */
    public abstract AbstractOperator getOperator();

    /** Is the class a Binary criterion? */
    public boolean isBinary() {
        return false;
    }

    /**
     * New rule instance
     * @param property UjoProperty
     * @param operator Operator
     * <ul>
     * <li>VALUE - the parameter value</li>
     * <li>UjoProperty - reference to a related entity</li>
     * <li>List&lt;TYPE&gt; - list of values (TODO - this type is planned in the future)</li>
     * </ul>
     * @return A new rule
     */
    public static <UJO extends Ujo, TYPE> Criterion<UJO> where(UjoProperty<UJO, TYPE> property, Operator operator, TYPE value) {
        return new ValueCriterion<UJO>(property, operator, value);
    }

    /**
     * New rule instance
     * @param property UjoProperty
     * @param operator Operator
     * <ul>
     * <li>VALUE - the parameter value</li>
     * <li>UjoProperty - reference to a related entity</li>
     * <li>List&lt;TYPE&gt; - list of values (TODO - this type is planned in the future)</li>
     * </ul>
     * @return A new rule
     */
    public static <UJO extends Ujo, TYPE> Rule<UJO> where(UjoProperty<UJO, TYPE> property, Operator operator, UjoProperty<?, TYPE> value) {
        return new ValueCriterion<UJO>(property, operator, value);
    }

    /**
     * New equals instance
     * @param property UjoProperty
     * <ul>
     * <li>TYPE - parameter value</li>
     * <li>List&lt;TYPE&gt; - list of values</li>
     * <li>UjoProperty - reference to a related entity</li>
     * </ul>
     * @return A the new immutable Rule
     */
    public static <UJO extends Ujo, TYPE> Criterion<UJO> where(UjoProperty<UJO, TYPE> property, TYPE value) {
        return new ValueCriterion<UJO>(property, null, value);
    }

    /**
     * Create new Rule for operator IN to compare value to a list of constants.
     * @param property A direct or indeirect Ujo property
     * @param list A collection of the values. The collection argument can be the EMPTY, the Rule result will be FALSE in this case.
     * @return A the new immutable Rule.
     */
    public static <UJO extends Ujo, TYPE> Criterion<UJO> whereIn(UjoProperty<UJO, TYPE> property, Collection<TYPE> list) {
        if (list.isEmpty()) {
            return Criterion.constant(property, false);
        } else {
            return new ValueCriterion<UJO>(property, Operator.IN, list.toArray());
        }
    }

    /**
     * Create new Rule for operator IN to compare value to a list of constants.
     * @param property A direct or indeirect Ujo property
     * @param list A collection of the values. The collection argument can be the EMPTY, the Rule result will be TRUE in this case.
     * @return A the new immutable Rule.
     */
    public static <UJO extends Ujo, TYPE> Criterion<UJO> whereNotIn(UjoProperty<UJO, TYPE> property, Collection<TYPE> list) {
        return new ValueCriterion<UJO>(property, Operator.NOT_IN, list.toArray());
    }

    /**
     * Create new Rule for operator IN to compare value to a list of constants
     * @param property A reference to a related entity
     * @param list A collection of the values. The collection argument can be the EMPTY, the Rule result will be FALSE in this case.
     * @return A the new immutable Rule
     */
    public static <UJO extends Ujo, TYPE> Criterion<UJO> whereIn(UjoProperty<UJO, TYPE> property, TYPE... list) {
        return new ValueCriterion<UJO>(property, Operator.IN, list);
    }

    /**
     * Create new Rule for operator IN to compare value to a list of constants.
     * @param A property direct or indeirect Ujo property
     * @param list A collection of the values. The collection argument can be the EMPTY, the Rule result will be TRUE in this case.
     * @return A the new immutable Rule.
     */
    public static <UJO extends Ujo, TYPE> Criterion<UJO> whereNotIn(UjoProperty<UJO, TYPE> property, TYPE... list) {
        return new ValueCriterion<UJO>(property, Operator.NOT_IN, list);
    }

    /**
     * New equals instance
     * @param property UjoProperty
     * @param value Value or UjoProperty can be type a direct of indirect (for a relation) property
     * @return A the new immutable Rule
     */
    public static <UJO extends Ujo, TYPE> Criterion<UJO> where(UjoProperty<UJO, TYPE> property, UjoProperty<UJO, TYPE> value) {
        return new ValueCriterion<UJO>(property, null, value);
    }

    /**
     * Rule where property equals to NULL.
     * @param property UjoProperty
     */
    public static <UJO extends Ujo, TYPE> Criterion<UJO> whereNull(UjoProperty<UJO, TYPE> property) {
        return new ValueCriterion<UJO>(property, Operator.EQ, (TYPE) null);
    }

    /**
     * Rule where property not equals to NULL.
     * @param property UjoProperty
     */
    public static <UJO extends Ujo, TYPE> Criterion<UJO> whereNotNull(UjoProperty<UJO, TYPE> property) {
        return new ValueCriterion<UJO>(property, Operator.NOT_EQ, (TYPE) null);
    }

    /** This is an constane rule independed on an entity.
     * It is recommended not to use this solution in ORM.
     */
    @SuppressWarnings("unchecked")
    public static <UJO extends Ujo> Criterion<UJO> where(boolean value) {
        return (Criterion<UJO>) (value ? ValueCriterion.TRUE : ValueCriterion.FALSE);
    }

    /** This is a special constant rule independed on the property or the ujo entity. A result is the same like the parameter constant allways.
     * @param property The parameter is required by Ujorm to location a basic database table and the join relations in case a composed Property
     * @see Operator#XFIXED
     */
    public static <UJO extends Ujo> Criterion<UJO> constant(UjoProperty<UJO, ?> property, boolean constant) {
        return new ValueCriterion<UJO>(property, Operator.XFIXED, constant);
    }

    /** The method creates a new Rule for a native condition (called Native Rule) in SQL statejemt format.
     * Special features:
     * <ul>
     *   <li>parameters of the SQL_condition are not supported by the Ujorm</li>
     *   <li>your own implementation of SQL the parameters can increase
     *       a risk of the <a href="http://en.wikipedia.org/wiki/SQL_injection">SQL injection</a> attacks</li>
     *   <li>method {@link #evaluate(org.ujoframework.Ujo)} is not supported and throws UnsupportedOperationException in the run-time</li>
     *   <li>native Rule dependents on a selected database so application developers should to create support for each supported database
     *       of target application to ensure database compatibility</li>
     * </ul>
     * @param property The parameter is required by Ujorm to location a basic database table and the join relations in case a composed Property
     * @param sqlCondition a SQL condition in the String format, the NULL value or empty string is not accepted
     * @see Operator#XSQL
     */
    public static <UJO extends Ujo> Criterion<UJO> forSql(UjoProperty<UJO, ?> property, String sqlCondition) {
        return new ValueCriterion<UJO>(property, Operator.XSQL, sqlCondition);
    }

    /**
     * New rule instance
     * @param property UjoProperty
     * @param operator Operator
     * <ul>
     * <li>VALUE - the parameter value</li>
     * <li>UjoProperty - reference to a related entity</li>
     * <li>List&lt;TYPE&gt; - list of values (TODO - this type is planned in the future)</li>
     * </ul>
     * @return A new rule
     * @deprecated See the {@link Rule#where(org.ujoframework.UjoProperty, org.ujoframework.rule.Operator, java.lang.Object) where(...) } method.
     */
    @Deprecated
    public static <UJO extends Ujo, TYPE> Criterion<UJO> newInstance(UjoProperty<UJO, TYPE> property, Operator operator, TYPE value) {
        return where(property, operator, value);
    }

    /**
     * New rule instance
     * @param property UjoProperty
     * @param operator Operator
     * <ul>
     * <li>VALUE - the parameter value</li>
     * <li>UjoProperty - reference to a related entity</li>
     * <li>List&lt;TYPE&gt; - list of values (TODO - this type is planned in the future)</li>
     * </ul>
     * @return A new rule
     * @deprecated See the {@link Rule#where(org.ujoframework.UjoProperty, org.ujoframework.rule.Operator, java.lang.Object) where(...) } method.
     */
    @Deprecated
    public static <UJO extends Ujo, TYPE> Rule<UJO> newInstance(UjoProperty<UJO, TYPE> property, Operator operator, UjoProperty<?, TYPE> value) {
        return where(property, operator, value);
    }

    /**
     * New equals instance
     * @param property UjoProperty
     * <ul>
     * <li>TYPE - parameter value</li>
     * <li>List&lt;TYPE&gt; - list of values</li>
     * <li>UjoProperty - reference to a related entity</li>
     * </ul>
     * @return A the new immutable Rule
     * @deprecated See the {@link Rule#where(org.ujoframework.UjoProperty, org.ujoframework.rule.Operator, java.lang.Object) where(...) } method.
     */
    @Deprecated
    public static <UJO extends Ujo, TYPE> Criterion<UJO> newInstance(UjoProperty<UJO, TYPE> property, TYPE value) {
        return where(property, value);
    }

    /**
     * New equals instance
     * @param property UjoProperty
     * @param value Value or UjoProperty can be type a direct of indirect (for a relation) property
     * @return A the new immutable Rule
     * @deprecated See the {@link Rule#where(org.ujoframework.UjoProperty, org.ujoframework.rule.Operator, java.lang.Object) where(...) } method.
     */
    @Deprecated
    public static <UJO extends Ujo, TYPE> Criterion<UJO> newInstance(UjoProperty<UJO, TYPE> property, UjoProperty<UJO, TYPE> value) {
        return where(property, value);
    }

    /** This is an constane rule independed on an entity.
     * It is recommended not to use this solution in ORM.
     * @deprecated See the {@link Rule#where(org.ujoframework.UjoProperty, org.ujoframework.rule.Operator, java.lang.Object) where(...) } method.
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public static <UJO extends Ujo> Criterion<UJO> newInstance(boolean value) {
        return where(value);
    }

    /** This is a constant rule independed on the property and the ujo entity. A result is the constantTrue always.
     * @deprecated See the {@link Rule#where(org.ujoframework.UjoProperty, org.ujoframework.rule.Operator, java.lang.Object) where(...) } method.
         */
    @Deprecated
    public static <UJO extends Ujo> Criterion<UJO> newInstanceTrue(UjoProperty<UJO, ?> property) {
        return constant(property, true);
    }

    /** This is a constant rule independed on the property and the ujo entity. A result is the constantFalse always.
     * @param <UJO>
     * @param property
     * @return
     * @deprecated See the {@link Rule#where(org.ujoframework.UjoProperty, org.ujoframework.rule.Operator, java.lang.Object) where(...) } method.
     */
    @Deprecated
    public static <UJO extends Ujo> Criterion<UJO> newInstanceFalse(UjoProperty<UJO, ?> property) {
        return constant(property, false);
    }
}
