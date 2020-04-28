package org.jdragon.springz.core.entry;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.04.28 16:05
 * @Description:
 */
public enum FilterType {
    /**
     * 按照注解类型过滤
     */
    ANNOTATION,

    /**
     * 按照类型过滤
     */
    ASSIGNABLE_TYPE,

    /**
     * 使用正则过滤
     */
    REGEX,

    /** 使用自定义规则过滤
     */
    CUSTOM
}

