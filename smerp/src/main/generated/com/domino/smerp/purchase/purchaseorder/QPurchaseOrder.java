package com.domino.smerp.purchase.purchaseorder;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPurchaseOrder is a Querydsl query type for PurchaseOrder
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPurchaseOrder extends EntityPathBase<PurchaseOrder> {

    private static final long serialVersionUID = -240002275L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPurchaseOrder purchaseOrder = new QPurchaseOrder("purchaseOrder");

    public final com.domino.smerp.common.QBaseEntity _super = new com.domino.smerp.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final StringPath documentNo = createString("documentNo");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final NumberPath<Long> poId = createNumber("poId", Long.class);

    public final NumberPath<java.math.BigDecimal> price = createNumber("price", java.math.BigDecimal.class);

    public final NumberPath<java.math.BigDecimal> qty = createNumber("qty", java.math.BigDecimal.class);

    public final StringPath remark = createString("remark");

    public final com.domino.smerp.purchase.requestorder.QRequestOrder requestOrder;

    public final NumberPath<java.math.BigDecimal> surtax = createNumber("surtax", java.math.BigDecimal.class);

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    public QPurchaseOrder(String variable) {
        this(PurchaseOrder.class, forVariable(variable), INITS);
    }

    public QPurchaseOrder(Path<? extends PurchaseOrder> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPurchaseOrder(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPurchaseOrder(PathMetadata metadata, PathInits inits) {
        this(PurchaseOrder.class, metadata, inits);
    }

    public QPurchaseOrder(Class<? extends PurchaseOrder> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.requestOrder = inits.isInitialized("requestOrder") ? new com.domino.smerp.purchase.requestorder.QRequestOrder(forProperty("requestOrder"), inits.get("requestOrder")) : null;
    }

}

