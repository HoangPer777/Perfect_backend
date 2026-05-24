package com.perfectmarket.modules.auth.security;

import java.util.UUID;

public interface CurrentUserProvider {
    UUID getCurrentUserId();
}
