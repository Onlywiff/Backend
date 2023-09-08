package com.onlywiff.backend.repository.converter;

import jakarta.persistence.AttributeConverter;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * A AttributeConverter to allow us the usage of byte arrays in entities.
 */
public class ByteToBlobAttributeConverter implements AttributeConverter<byte[], Blob> {

    /**
     * @inheritDoc
     */
    @Override
    public byte[] convertToEntityAttribute(Blob attribute) {
        try {
            int blobLength = (int) attribute.length();
            byte[] bytes = attribute.getBytes(1, blobLength);
            attribute.free();
            return bytes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public Blob convertToDatabaseColumn(byte[] dbData) {
        try {
            return new SerialBlob(dbData);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}