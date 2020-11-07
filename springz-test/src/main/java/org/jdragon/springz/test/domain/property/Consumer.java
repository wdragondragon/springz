package org.jdragon.springz.test.domain.property;

import lombok.Data;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.core.annotation.Value;

import java.util.List;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.07 18:52
 * @Description:
 */
@Data
@Component
public class Consumer {

    @Value("${reflex.customer.firstName}")
    private String firstName;

    @Value("${reflex.customer.lastName}")
    private String lastName;

    @Value("${reflex.customer.age}")
    private Integer age;

    @Value("${reflex.customer.contactDetails}")
    private Contact[] contactDetails;

    @Value("${reflex.customer.contactDetails}")
    private List<Contact> contactDetailList;

    @Value("${reflex.customer.hobby}")
    private Map<String, String> hobby;

    @Value("${reflex.customer.friends}")
    private Friend[] friends;

    @Value("${reflex.customer.friends}")
    private List<Friend> friendList;
}