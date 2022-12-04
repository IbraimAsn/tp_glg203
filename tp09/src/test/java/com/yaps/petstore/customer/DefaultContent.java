package com.yaps.petstore.customer;

import com.yaps.petstore.customer.domain.Customer;
import com.yaps.petstore.security.domain.YapsUser;
import com.yaps.petstore.security.domain.YapsUserRole;

/**
 * Utility class for holding the content of the default test DB for DBUnit.
 */
public class DefaultContent {

    private DefaultContent() {
    }

      
    /**
     * The categories found in the default test setting.
     */
    public static final Customer[] DEFAULT_CUSTOMER = {
            new Customer("1", "fn1", "ln1",
                    "1", "c1@cnam.fr", "s11", "s21", "c1",
                    "s1", "z1", "c1",
                        new YapsUser("u1", YapsUserRole.CUST)
                    ),
            new Customer("2", "fn2", "ln2",
                    "2", "c2@cnam.fr", "s12", "s22", "c2",
                    "s2", "z2", "c2",
                    new YapsUser("u2", YapsUserRole.CUST)
                    )
    };
 
}
