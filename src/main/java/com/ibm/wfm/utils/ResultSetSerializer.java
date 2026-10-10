package com.ibm.wfm.utils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import tools.jackson.core.JsonGenerator;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class ResultSetSerializer extends ValueSerializer<ResultSet> {
    //private static ValueSerializer<Object> objectSerializer = null;
    private static ValueSerializer<Object> clobSerializer   = null;
    private static ValueSerializer<Object> blobSerializer   = null;

    public static class ResultSetSerializerException extends JacksonException{
        private static final long serialVersionUID = -914957626413580734L;

        public ResultSetSerializerException(Throwable cause){
            super(cause);
        }
    }

    @Override
    public Class<ResultSet> handledType() {
        return ResultSet.class;
    }

    @Override
    public void serialize(ResultSet rs, JsonGenerator jgen, SerializationContext context) throws JacksonException {

        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColumns = rsmd.getColumnCount();
            String[] columnNames = new String[numColumns];
            int[] columnTypes = new int[numColumns];

            for (int i = 0; i < columnNames.length; i++) {
                columnNames[i] = rsmd.getColumnLabel(i + 1);
                columnTypes[i] = rsmd.getColumnType(i + 1);
            }

            jgen.writeStartArray();

            while (rs.next()) {

                boolean b;
                long l;
                double d;

                jgen.writeStartObject();

                for (int i = 0; i < columnNames.length; i++) {

                    jgen.writeName(columnNames[i]);
                    switch (columnTypes[i]) {

                    case Types.INTEGER:
                        l = rs.getInt(i + 1);
                        if (rs.wasNull()) {
                            jgen.writeNull();
                        } else {
                            jgen.writeNumber(l);
                        }
                        break;

                    case Types.BIGINT:
                        l = rs.getLong(i + 1);
                        if (rs.wasNull()) {
                            jgen.writeNull();
                        } else {
                            jgen.writeNumber(l);
                        }
                        break;

                    case Types.DECIMAL:
                    case Types.NUMERIC:
                        BigDecimal bigDecimal = rs.getBigDecimal(i + 1);
                        if (rs.wasNull())
                            jgen.writeNull();
                        else
                            jgen.writeNumber(bigDecimal);
                        break;

                    case Types.FLOAT:
                    case Types.REAL:
                    case Types.DOUBLE:
                        d = rs.getDouble(i + 1);
                        if (rs.wasNull()) {
                            jgen.writeNull();
                        } else {
                            jgen.writeNumber(d);
                        }
                        break;

                    case Types.CHAR:
                    case Types.NCHAR:
                    case Types.NVARCHAR:
                    case Types.VARCHAR:
                    case Types.LONGNVARCHAR:
                    case Types.LONGVARCHAR:
                        String string = rs.getString(i + 1);
                        if (rs.wasNull())
                            jgen.writeNull();
                        else
                            jgen.writeString(string);
                        break;

                    case Types.BOOLEAN:
                    case Types.BIT:
                        b = rs.getBoolean(i + 1);
                        if (rs.wasNull()) {
                            jgen.writeNull();
                        } else {
                            jgen.writeBoolean(b);
                        }
                        break;

                    case Types.BINARY:
                    case Types.VARBINARY:
                    case Types.LONGVARBINARY:
                        byte[] bytes = rs.getBytes(i + 1);
                        if (rs.wasNull())
                            jgen.writeNull();
                        else {
                            jgen.writeBinary(bytes);
                        }
                        break;

                    case Types.TINYINT:
                    case Types.SMALLINT:
                        l = rs.getShort(i + 1);
                        if (rs.wasNull()) {
                            jgen.writeNull();
                        } else {
                            jgen.writeNumber(l);
                        }
                        break;

                    case Types.DATE:
                    	java.sql.Date dt = rs.getDate(i + 1);
                    	if (dt==null)
                    		jgen.writeNull();
                    	else
                    		context.defaultSerializeDateValue(rs.getDate(i + 1), jgen);
                        break;

                    case Types.TIMESTAMP:
                    	Timestamp ts = rs.getTimestamp(i + 1);
                    	if (ts==null)
                    		jgen.writeNull();
                    	else
                    		context.defaultSerializeDateValue(rs.getTimestamp(i + 1), jgen);
                        break;

                    case Types.BLOB:
                        Blob blob = rs.getBlob(i);
                        if (rs.wasNull())
                            jgen.writeNull();
                        else {
                            if (blobSerializer == null) blobSerializer = context.findValueSerializer(Blob.class);
                            blobSerializer.serialize(blob, jgen, context);
                        }
                        blob.free();
                        break;

                    case Types.CLOB:
                        Clob clob = rs.getClob(i);
                        if (clobSerializer == null) clobSerializer = context.findValueSerializer(Clob.class);
                        clobSerializer.serialize(clob, jgen, context);
                        clob.free();
                        break;

                    case Types.ARRAY:
                        throw new RuntimeException("ResultSetSerializer not yet implemented for SQL type ARRAY");

                    case Types.STRUCT:
                        throw new RuntimeException("ResultSetSerializer not yet implemented for SQL type STRUCT");

                    case Types.DISTINCT:
                        throw new RuntimeException("ResultSetSerializer not yet implemented for SQL type DISTINCT");

                    case Types.REF:
                        throw new RuntimeException("ResultSetSerializer not yet implemented for SQL type REF");

                    //insures that the serializer returned matches the exact class being serialized.
                    case Types.JAVA_OBJECT:
                    default:
                        Object object = rs.getObject(i + 1);
                        if (rs.wasNull())
                            jgen.writeNull();
                        else {
                        	ValueSerializer<Object> serializer =
                        		    context.findValueSerializer(object.getClass());

                        	serializer.serialize(object, jgen, context);
                        }
                        break;
                    }
                }

                jgen.writeEndObject();
            }

            jgen.writeEndArray();

        } catch (SQLException e) {
            throw new ResultSetSerializerException(e);
        }
    }
}