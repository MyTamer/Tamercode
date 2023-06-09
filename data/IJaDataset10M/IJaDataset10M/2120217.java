package org.datanucleus.store.mapped;

import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.AbstractMemberMetaData;

/**
 * Factory that creates immutable instances of DatastoreIdentifier.
 * Identifiers are of a particular type. Each datastore could invent its own  particular types
 * as required, just that the ones here should be the principal types required.
 */
public interface IdentifierFactory {

    /**
     * Accessor for the datastore adapter that we are creating identifiers for.
     * @return The datastore adapter
     */
    DatastoreAdapter getDatastoreAdapter();

    /**
     * Accessor for the identifier case being used.
     * @return The identifier case
     */
    IdentifierCase getIdentifierCase();

    /**
     * Accessor for an identifier for use in the datastore adapter
     * @param identifier The identifier name
     * @return Identifier name for use with the datastore adapter
     */
    String getIdentifierInAdapterCase(String identifier);

    /**
     * To be called when we want an identifier name creating based on the
     * identifier. Creates identifier for COLUMN, FOREIGN KEY, INDEX and TABLE
     * @param identifierType the type of identifier to be created
     * @param identifierName The identifier name
     * @return The DatastoreIdentifier
     */
    DatastoreIdentifier newIdentifier(IdentifierType identifierType, String identifierName);

    /**
     * Method to use to generate an identifier for a datastore field with the supplied name.
     * The passed name will not be changed (other than in its case) although it may
     * be truncated to fit the maximum length permitted for a datastore field identifier.
     * @param identifierName The identifier name
     * @return The DatastoreIdentifier for the table
     */
    DatastoreIdentifier newDatastoreContainerIdentifier(String identifierName);

    /**
     * Method to return a Table identifier for the specified class.
     * @param md Meta data for the class
     * @return The identifier for the table
     */
    DatastoreIdentifier newDatastoreContainerIdentifier(AbstractClassMetaData md);

    /**
     * Method to return a Table identifier for the specified field.
     * @param fmd Meta data for the field
     * @return The identifier for the table
     */
    DatastoreIdentifier newDatastoreContainerIdentifier(AbstractMemberMetaData fmd);

    /**
     * Method to use to generate an identifier for a datastore field with the supplied name.
     * The passed name will not be changed (other than in its case) although it may
     * be truncated to fit the maximum length permitted for a datastore field identifier.
     * @param identifierName The identifier name
     * @return The DatastoreIdentifier
     */
    DatastoreIdentifier newDatastoreFieldIdentifier(String identifierName);

    /**
     * Method to create an identifier for a datastore field where we want the
     * name based on the supplied java name, and the field has a particular
     * role (and so could have its naming set according to the role).
     * @param javaName The java field name
     * @param embedded Whether the identifier is for a field embedded
     * @param fieldRole The role to be performed by this column e.g FK, Index ?
     * @return The DatastoreIdentifier
     */
    DatastoreIdentifier newDatastoreFieldIdentifier(String javaName, boolean embedded, int fieldRole);

    /**
     * Method to generate an identifier name for reference field, based on the metadata for the
     * field, and the ClassMetaData for the implementation.
     * @param refMetaData the MetaData for the reference field
     * @param implMetaData the AbstractClassMetaData for this implementation
     * @param implIdentifier PK identifier for the implementation
     * @param embedded Whether the identifier is for a field embedded
     * @param fieldRole The role to be performed by this column e.g FK, collection element ?
     * @return The DatastoreIdentifier
     */
    DatastoreIdentifier newReferenceFieldIdentifier(AbstractMemberMetaData refMetaData, AbstractClassMetaData implMetaData, DatastoreIdentifier implIdentifier, boolean embedded, int fieldRole);

    /**
     * Method to return an identifier for a discriminator datastore field.
     * @return The discriminator datastore field identifier
     */
    DatastoreIdentifier newDiscriminatorFieldIdentifier();

    /**
     * Method to return an identifier for a version datastore field.
     * @return The version datastore field identifier
     */
    DatastoreIdentifier newVersionFieldIdentifier();

    /**
     * Method to return a new Identifier based on the passed identifier, but adding on the passed suffix
     * @param identifier The current identifier
     * @param suffix The suffix
     * @return The new identifier
     */
    DatastoreIdentifier newIdentifier(DatastoreIdentifier identifier, String suffix);

    /**
     * Method to generate a join-table identifier. The identifier could be for a foreign-key
     * to another table (if the destinationId is provided), or could be for a simple column
     * in the join table.
     * @param ownerFmd MetaData for the owner field
     * @param relatedFmd MetaData for the related field (if bidirectional)
     * @param destinationId Identifier for the identity field of the destination table
     * @param embedded Whether the identifier is for a field embedded
     * @param fieldRole The role to be performed by this column e.g FK, collection element ?
     * @return The identifier.
     */
    DatastoreIdentifier newJoinTableFieldIdentifier(AbstractMemberMetaData ownerFmd, AbstractMemberMetaData relatedFmd, DatastoreIdentifier destinationId, boolean embedded, int fieldRole);

    /**
     * Method to generate a FK/FK-index field identifier. 
     * The identifier could be for the FK field itself, or for a related index for the FK.
     * @param ownerFmd MetaData for the owner field
     * @param relatedFmd MetaData for the related field (if bidirectional)
     * @param destinationId Identifier for the identity field of the destination table (if strict FK)
     * @param embedded Whether the identifier is for a field embedded
     * @param fieldRole The role to be performed by this column e.g owner, index ?
     * @return The identifier
     */
    DatastoreIdentifier newForeignKeyFieldIdentifier(AbstractMemberMetaData ownerFmd, AbstractMemberMetaData relatedFmd, DatastoreIdentifier destinationId, boolean embedded, int fieldRole);

    /**
     * Method to return an identifier for an index (ordering) datastore field.
     * @param mmd Metadata for the field/property that we require to add an index(order) column for
     * @return The index datastore field identifier
     */
    DatastoreIdentifier newIndexFieldIdentifier(AbstractMemberMetaData mmd);

    /**
     * Method to return an identifier for an adapter index datastore field.
     * An "adapter index" is a column added to be part of a primary key when some other
     * column cant perform that role.
     * @return The index datastore field identifier
     */
    DatastoreIdentifier newAdapterIndexFieldIdentifier();

    /**
     * Method to generate an identifier for a sequence using the passed name.
     * @param sequenceName the name of the sequence to use
     * @return The DatastoreIdentifier
     */
    DatastoreIdentifier newSequenceIdentifier(String sequenceName);

    /**
     * Method to generate an identifier for a primary key.
     * @param table the table
     * @return The DatastoreIdentifier
     */
    DatastoreIdentifier newPrimaryKeyIdentifier(DatastoreContainerObject table);

    /**
     * Method to generate an identifier for an index.
     * @param table the table
     * @param isUnique if the index is unique
     * @param seq the sequential number
     * @return The DatastoreIdentifier
     */
    DatastoreIdentifier newIndexIdentifier(DatastoreContainerObject table, boolean isUnique, int seq);

    /**
     * Method to generate an identifier for a candidate key.
     * @param table the table
     * @param seq Sequence number
     * @return The DatastoreIdentifier
     */
    DatastoreIdentifier newCandidateKeyIdentifier(DatastoreContainerObject table, int seq);

    /**
     * Method to create an identifier for a foreign key.
     * @param table the table
     * @param seq the sequential number
     * @return The DatastoreIdentifier
     */
    DatastoreIdentifier newForeignKeyIdentifier(DatastoreContainerObject table, int seq);
}
