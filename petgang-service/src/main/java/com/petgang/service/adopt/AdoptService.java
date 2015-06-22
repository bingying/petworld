package com.petgang.service.adopt;

import com.petgang.model.adopt.Adopt;
import com.petgang.model.adopt.AdoptStatus;

public interface AdoptService {

    public int save(Adopt adopt);

    public int publish(int id);

    public int adopting(int id);

    public int adopted(int id);

    public int changeStatus(int id, AdoptStatus statusE);
}
