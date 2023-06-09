package org.wltea.expression.op.define;

import org.wltea.expression.IllegalExpressionException;
import org.wltea.expression.datameta.BaseDataMeta;
import org.wltea.expression.datameta.Constant;
import org.wltea.expression.datameta.Reference;
import org.wltea.expression.op.IOperatorExecution;
import org.wltea.expression.op.Operator;

/**
 * 加号操作实现
 * 加号操作包括了数学加法和字符窜连接
 * @author 林良益，卓诗垚
 * @version 2.0 
 */
public class Op_PLUS implements IOperatorExecution {

    public static final Operator THIS_OPERATOR = Operator.PLUS;

    public Constant execute(Constant[] args) throws IllegalExpressionException {
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException("操作符\"" + THIS_OPERATOR.getToken() + "参数个数不匹配");
        }
        Constant first = args[1];
        Constant second = args[0];
        if (first == null || second == null) {
            throw new NullPointerException("操作符\"" + THIS_OPERATOR.getToken() + "\"参数为空");
        }
        if (first.isReference()) {
            Reference firstRef = (Reference) first.getDataValue();
            first = firstRef.execute();
        }
        if (second.isReference()) {
            Reference secondRef = (Reference) second.getDataValue();
            second = secondRef.execute();
        }
        if (BaseDataMeta.DataType.DATATYPE_LIST == first.getDataType() || BaseDataMeta.DataType.DATATYPE_LIST == second.getDataType()) {
            throw new IllegalArgumentException("操作符\"" + THIS_OPERATOR.getToken() + "\"参数类型错误");
        } else if (BaseDataMeta.DataType.DATATYPE_STRING == first.getDataType() || BaseDataMeta.DataType.DATATYPE_STRING == second.getDataType() || BaseDataMeta.DataType.DATATYPE_NULL == first.getDataType() || BaseDataMeta.DataType.DATATYPE_NULL == second.getDataType() || BaseDataMeta.DataType.DATATYPE_BOOLEAN == first.getDataType() || BaseDataMeta.DataType.DATATYPE_BOOLEAN == second.getDataType() || BaseDataMeta.DataType.DATATYPE_DATE == first.getDataType() || BaseDataMeta.DataType.DATATYPE_DATE == second.getDataType()) {
            String firstString = "";
            String secondString = "";
            if (null != first.getStringValue()) {
                firstString = first.getStringValue();
            }
            if (null != second.getStringValue()) {
                secondString = second.getStringValue();
            }
            String result = firstString + secondString;
            return new Constant(BaseDataMeta.DataType.DATATYPE_STRING, result);
        } else if (null == first.getDataValue() || null == second.getDataValue()) {
            throw new NullPointerException("操作符\"" + THIS_OPERATOR.getToken() + "\"参数为空");
        } else if (BaseDataMeta.DataType.DATATYPE_DOUBLE == first.getDataType() || BaseDataMeta.DataType.DATATYPE_DOUBLE == second.getDataType()) {
            Double result = first.getDoubleValue() + second.getDoubleValue();
            return new Constant(BaseDataMeta.DataType.DATATYPE_DOUBLE, result);
        } else if (BaseDataMeta.DataType.DATATYPE_FLOAT == first.getDataType() || BaseDataMeta.DataType.DATATYPE_FLOAT == second.getDataType()) {
            Float result = first.getFloatValue() + second.getFloatValue();
            return new Constant(BaseDataMeta.DataType.DATATYPE_FLOAT, result);
        } else if (BaseDataMeta.DataType.DATATYPE_LONG == first.getDataType() || BaseDataMeta.DataType.DATATYPE_LONG == second.getDataType()) {
            Long result = first.getLongValue() + second.getLongValue();
            return new Constant(BaseDataMeta.DataType.DATATYPE_LONG, result);
        } else {
            Integer result = first.getIntegerValue() + second.getIntegerValue();
            return new Constant(BaseDataMeta.DataType.DATATYPE_INT, result);
        }
    }

    public Constant verify(int opPositin, BaseDataMeta[] args) throws IllegalExpressionException {
        if (args == null) {
            throw new IllegalArgumentException("运算操作符参数为空");
        }
        if (args.length != 2) {
            throw new IllegalExpressionException("操作符\"" + THIS_OPERATOR.getToken() + "\"参数个数不匹配", THIS_OPERATOR.getToken(), opPositin);
        }
        BaseDataMeta first = args[1];
        BaseDataMeta second = args[0];
        if (first == null || second == null) {
            throw new NullPointerException("操作符\"" + THIS_OPERATOR.getToken() + "\"参数为空");
        }
        if (BaseDataMeta.DataType.DATATYPE_LIST == first.getDataType() || BaseDataMeta.DataType.DATATYPE_LIST == second.getDataType()) {
            throw new IllegalExpressionException("操作符\"" + THIS_OPERATOR.getToken() + "\"参数类型错误", THIS_OPERATOR.getToken(), opPositin);
        }
        if (BaseDataMeta.DataType.DATATYPE_STRING == first.getDataType() || BaseDataMeta.DataType.DATATYPE_STRING == second.getDataType() || BaseDataMeta.DataType.DATATYPE_NULL == first.getDataType() || BaseDataMeta.DataType.DATATYPE_NULL == second.getDataType() || BaseDataMeta.DataType.DATATYPE_BOOLEAN == first.getDataType() || BaseDataMeta.DataType.DATATYPE_BOOLEAN == second.getDataType() || BaseDataMeta.DataType.DATATYPE_DATE == first.getDataType() || BaseDataMeta.DataType.DATATYPE_DATE == second.getDataType()) {
            return new Constant(BaseDataMeta.DataType.DATATYPE_STRING, null);
        } else if (BaseDataMeta.DataType.DATATYPE_DOUBLE == first.getDataType() || BaseDataMeta.DataType.DATATYPE_DOUBLE == second.getDataType()) {
            return new Constant(BaseDataMeta.DataType.DATATYPE_DOUBLE, Double.valueOf(0.0));
        } else if (BaseDataMeta.DataType.DATATYPE_FLOAT == first.getDataType() || BaseDataMeta.DataType.DATATYPE_FLOAT == second.getDataType()) {
            return new Constant(BaseDataMeta.DataType.DATATYPE_FLOAT, Float.valueOf(0.0f));
        } else if (BaseDataMeta.DataType.DATATYPE_LONG == first.getDataType() || BaseDataMeta.DataType.DATATYPE_LONG == second.getDataType()) {
            return new Constant(BaseDataMeta.DataType.DATATYPE_LONG, Long.valueOf(0l));
        } else {
            return new Constant(BaseDataMeta.DataType.DATATYPE_INT, Integer.valueOf(0));
        }
    }
}
