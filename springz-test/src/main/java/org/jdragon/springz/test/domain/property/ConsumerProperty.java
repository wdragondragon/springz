package org.jdragon.springz.test.domain.property;

import lombok.Data;
import org.jdragon.springz.core.annotation.Component;
import org.jdragon.springz.core.annotation.Properties;
import org.jdragon.springz.core.annotation.Value;

import java.util.List;
import java.util.Map;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.11.07 19:13
 * @Description:
 */
@Data
@Component
@Properties(prefix = "reflex.customer")
public class ConsumerProperty {

    @Value("${firstName}")
    private String firstName;

    @Value("${lastName}")
    private String lastName;

    @Value("${age}")
    private Integer age;

    @Value("${contactDetails}")
    private Contact[] contactDetails;

    @Value("${contactDetails}")
    private List<Contact> contactDetailList;

    @Value("${hobby}")
    private Map<String, String> hobby;

    @Value("${friends}")
    private Friend[] friends;

    @Value("${friends}")
    private List<Friend> friendList;

}
