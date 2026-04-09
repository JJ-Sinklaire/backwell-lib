package com.backwell.enums;

import com.backwell.exception.UnknownRoleException;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum RoleName{
    OWNER(0),
    ADMIN(1),
    MANAGER(2),
    USER(3);

    private final int level;

    RoleName(int level) {
        this.level = level;
    }

    public boolean canCreate(RoleName targetRole) {
        if (this == OWNER) return true;

        if (this == USER && targetRole ==USER) return true;

        return this.level < targetRole.level;
    }

    public boolean canRevoke(RoleName targetRole) {
        Objects.requireNonNull(targetRole, "Target role can't be null");

        if (this == OWNER) return true;
        if (targetRole == OWNER) return false;
        return this.level < targetRole.level;
    }

    private static final Map<String, RoleName> LOOKUP = Arrays.stream(values())
            .collect(Collectors.toMap(
                    r->r.name().toLowerCase(),
                    Function.identity()
            ));


    public static RoleName getHighestRole(Set<RoleName> roles) {
        if  (roles == null || roles.isEmpty()) return null;
        return roles.stream()
                .min(
                        Comparator.comparingInt(r-> r.level)
                )
                .orElse(USER);
    }

    public static RoleName fromString(String name) {
        RoleName role = LOOKUP.get(name.toLowerCase());
        if (role == null) throw new UnknownRoleException("Unknown role: " + name);
        return role;
    }


}