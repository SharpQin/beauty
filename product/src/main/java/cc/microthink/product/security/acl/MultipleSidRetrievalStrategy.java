package cc.microthink.product.security.acl;

import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements multiple PrincipalSid
 * SidRetrievalStrategyImpl
 */
public class MultipleSidRetrievalStrategy implements SidRetrievalStrategy {

    @Override
    public List<Sid> getSids(Authentication authentication) {
        List<Sid> sids = new ArrayList<>(2);
        sids.add(new PrincipalSid(authentication));

        //Test
        sids.add(new PrincipalSid("user"));

        return sids;
    }
}
