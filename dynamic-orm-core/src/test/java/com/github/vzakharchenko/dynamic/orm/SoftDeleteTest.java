package com.github.vzakharchenko.dynamic.orm;

import org.testng.annotations.Test;
import com.github.vzakharchenko.dynamic.orm.core.dynamic.QDynamicTable;
import com.github.vzakharchenko.dynamic.orm.core.dynamic.dml.DynamicTableModel;
import com.github.vzakharchenko.dynamic.orm.core.pk.UUIDPKGenerator;
import com.github.vzakharchenko.dynamic.orm.model.TestTableStatus;
import com.github.vzakharchenko.dynamic.orm.qModel.QTestTableStatus;

import static org.testng.Assert.*;

/**
 *
 */
public class SoftDeleteTest extends OracleTestQueryOrm {

    @Test
    public void testSoftDelete() {

        TestTableStatus testTableStatus = new TestTableStatus();
        testTableStatus.setTestColumn(12345);
        ormQueryFactory.insert(testTableStatus);
        assertNotNull(testTableStatus.getId());
        assertNotNull(testTableStatus.getVersion());
        assertEquals(testTableStatus.getVersion(), Integer.valueOf(0));
        assertEquals(testTableStatus.getStatus(), Integer.valueOf(0));

        TestTableStatus testTableStatusCache = ormQueryFactory.modelCacheBuilder(TestTableStatus.class).findOneByColumn(QTestTableStatus.qTestTableStatus.testColumn, 12345);
        assertNotNull(testTableStatusCache);
        assertNotNull(testTableStatusCache.getId());
        assertNotNull(testTableStatusCache.getVersion());
        assertEquals(testTableStatusCache.getVersion(), Integer.valueOf(0));
        assertEquals(testTableStatusCache.getStatus(), Integer.valueOf(0));

        ormQueryFactory.softDeleteById(testTableStatus);

        TestTableStatus newTableStatus = ormQueryFactory.select().findOne(ormQueryFactory.buildQuery().from(QTestTableStatus.qTestTableStatus), TestTableStatus.class);
        assertEquals(newTableStatus.getStatus(), Integer.valueOf(-1));


        TestTableStatus testTableStatusCacheAfterDeletion = ormQueryFactory.modelCacheBuilder(TestTableStatus.class).findOneByColumn(QTestTableStatus.qTestTableStatus.testColumn, 12345);
        assertNull(testTableStatusCacheAfterDeletion);

    }

    @Test
    public void testDynamicSoftDelete() {

        qDynamicTableFactory.buildTable("TEST_DYNAMIC_SOFT_DELETE_TABLE")
                .addPrimaryStringKey("ID", 100)
                .addPrimaryKeyGenerator(UUIDPKGenerator.getInstance())
                .createNumberColumn("VERSION", Integer.class, 38, 0, true).addVersionColumn("VERSION")
                .createNumberColumn("STATUS", Integer.class, 38, 0, true).addSoftDeleteColumn("STATUS", -1, 0)
                .createNumberColumn("TEST_COLUMN", Integer.class, 38, 0, true)
                .buildSchema();

        QDynamicTable qDynamicTable = qDynamicTableFactory.getQDynamicTableByName("TEST_DYNAMIC_SOFT_DELETE_TABLE");
        DynamicTableModel testTableVersion = new DynamicTableModel(qDynamicTable);
        testTableVersion.addColumnValue("TEST_COLUMN", 123456);
        ormQueryFactory.insert(testTableVersion);

        assertNotNull(testTableVersion.getValue("ID"));
        assertNotNull(testTableVersion.getValue("VERSION"));
        assertEquals(testTableVersion.getValue("VERSION", Integer.class), Integer.valueOf(0));
        assertEquals(testTableVersion.getValue("STATUS", Integer.class), Integer.valueOf(0));
        assertEquals(testTableVersion.getValue("TEST_COLUMN", Integer.class), Integer.valueOf(123456));

        DynamicTableModel dynamicTableModelCache = ormQueryFactory.modelCacheBuilder(qDynamicTable, DynamicTableModel.class).findOneByColumn(qDynamicTable.getNumberColumnByName("TEST_COLUMN"), 123456);
        assertNotNull(dynamicTableModelCache);
        assertNotNull(dynamicTableModelCache.getValue("ID"));
        assertNotNull(dynamicTableModelCache.getValue("VERSION"));
        assertEquals(dynamicTableModelCache.getValue("VERSION", Integer.class), Integer.valueOf(0));
        assertEquals(dynamicTableModelCache.getValue("STATUS", Integer.class), Integer.valueOf(0));
        assertEquals(dynamicTableModelCache.getValue("TEST_COLUMN", Integer.class), Integer.valueOf(123456));

        ormQueryFactory.softDeleteById(testTableVersion);

        DynamicTableModel dynamicTableModel = ormQueryFactory.select().findOne(ormQueryFactory.buildQuery().from(qDynamicTable), qDynamicTable, DynamicTableModel.class);
        assertNotNull(dynamicTableModel);
        assertEquals(dynamicTableModel.getValue("STATUS", Integer.class), Integer.valueOf(-1));


        DynamicTableModel dynamicTableModelCacheAfterDeletion = ormQueryFactory.modelCacheBuilder(qDynamicTable, DynamicTableModel.class).findOneByColumn(qDynamicTable.getNumberColumnByName("TEST_COLUMN"), 123456);
        assertNull(dynamicTableModelCacheAfterDeletion);

    }
}