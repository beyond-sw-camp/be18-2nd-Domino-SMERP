package com.domino.smerp.purchase.requestorder;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRequestOrder is a Querydsl query type for RequestOrder
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRequestOrder extends EntityPathBase<RequestOrder> {

    private static final long serialVersionUID = -1527054465L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRequestOrder requestOrder = new QRequestOrder("requestOrder");

    public final com.domino.smerp.common.QBaseEntity _super = new com.domino.smerp.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final com.domino.smerp.purchase.purchaseorder.QPurchaseOrder purchaseOrder;

    public final NumberPath<Long> roId = createNumber("roId", Long.class);

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    public QRequestOrder(String variable) {
        this(RequestOrder.class, forVariable(variable), INITS);
    }

    public QRequestOrder(Path<? extends RequestOrder> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRequestOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRequestOrder(PathMetadata metadata, PathInits inits) {
        this(RequestOrder.class, metadata, inits);
    }

    public QRequestOrder(Class<? extends RequestOrder> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.purchaseOrder = inits.isInitialized("purchaseOrder") ? new com.domino.smerp.purchase.purchaseorder.QPurchaseOrder(forProperty("purchaseOrder"), inits.get("purchaseOrder")) : null;
    }

}

