package com.domino.smerp.order;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrder is a Querydsl query type for Order
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOrder extends EntityPathBase<Order> {

    private static final long serialVersionUID = -996200016L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrder order = new QOrder("order1");

    public final com.domino.smerp.common.QBaseEntity _super = new com.domino.smerp.common.QBaseEntity(this);

    public final com.domino.smerp.client.QClient client;

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final DateTimePath<java.time.Instant> deliveryDate = createDateTime("deliveryDate", java.time.Instant.class);

    public final StringPath documentNo = createString("documentNo");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Long> orderId = createNumber("orderId", Long.class);

    public final ListPath<com.domino.smerp.itemorder.ItemOrderCrossedTable, com.domino.smerp.itemorder.QItemOrderCrossedTable> orderItems = this.<com.domino.smerp.itemorder.ItemOrderCrossedTable, com.domino.smerp.itemorder.QItemOrderCrossedTable>createList("orderItems", com.domino.smerp.itemorder.ItemOrderCrossedTable.class, com.domino.smerp.itemorder.QItemOrderCrossedTable.class, PathInits.DIRECT2);

    public final StringPath remark = createString("remark");

    public final EnumPath<com.domino.smerp.order.constants.OrderStatus> status = createEnum("status", com.domino.smerp.order.constants.OrderStatus.class);

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    public final com.domino.smerp.user.QUser user;

    public QOrder(String variable) {
        this(Order.class, forVariable(variable), INITS);
    }

    public QOrder(Path<? extends Order> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrder(PathMetadata metadata, PathInits inits) {
        this(Order.class, metadata, inits);
    }

    public QOrder(Class<? extends Order> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.client = inits.isInitialized("client") ? new com.domino.smerp.client.QClient(forProperty("client")) : null;
        this.user = inits.isInitialized("user") ? new com.domino.smerp.user.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

