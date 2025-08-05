package com.ruoyi.clock_in.service;

import com.ruoyi.clock_in.domain.AuthDto;
import com.ruoyi.clock_in.domain.vo.AuthVo;
import org.springframework.stereotype.Service;

/**
 * <p>
 *
 * </p>
 *
 * @author lzt
 * @version 1.0
 * @since 2024/9/29 下午9:33
 */

public interface AuthService {

    public AuthVo login(AuthDto authDto);
}
