package com.domino.smerp.itemorder;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemOrderCrossedTable is a Querydsl query type for ItemOrderCrossedTable
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemOrderCrossedTable extends EntityPathBase<ItemOrderCrossedTable> {

    private static final long serialVersionUID = 68920377L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemOrderCrossedTable itemOrderCrossedTable = new QItemOrderCrossedTable("itemOrderCrossedTable");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.domino.smerp.item.QItem item;

    public final com.domino.smerp.order.QOrder order;

    public final NumberPath<java.math.BigDecimal> qty = createNumber("qty", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> specialPrice = createNumber("specialPrice", java.math.BigDecimal.class);

    public QItemOrderCrossedTable(String variable) {
        this(ItemOrderCrossedTable.class, forVariable(variable), INITS);
    }

    public QItemOrderCrossedTable(Path<? extends ItemOrderCrossedTable> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemOrderCrossedTable(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemOrderCrossedTable(PathMetadata metadata, PathInits inits) {
        this(ItemOrderCrossedTable.class, metadata, inits);
    }

    public QItemOrderCrossedTable(Class<? extends ItemOrderCrossedTable> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new com.domino.smerp.item.QItem(forProperty("item"), inits.get("item")) : null;
        this.order = inits.isInitialized("order") ? new com.domino.smerp.order.QOrder(forProperty("order"), inits.get("order")) : null;
    }

}

