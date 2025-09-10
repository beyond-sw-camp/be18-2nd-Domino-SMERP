package com.domino.smerp.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -905998896L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final StringPath address = createString("address");

    public final com.domino.smerp.client.QClient client;

    public final StringPath deptTitle = createString("deptTitle");

    public final StringPath email = createString("email");

    public final StringPath empNo = createString("empNo");

    public final DatePath<java.time.LocalDate> fireDate = createDate("fireDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> hireDate = createDate("hireDate", java.time.LocalDate.class);

    public final StringPath loginId = createString("loginId");

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final StringPath phone = createString("phone");

    public final EnumPath<com.domino.smerp.user.constants.UserRole> role = createEnum("role", com.domino.smerp.user.constants.UserRole.class);

    public final StringPath ssn = createString("ssn");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.client = inits.isInitialized("client") ? new com.domino.smerp.client.QClient(forProperty("client")) : null;
    }

}

