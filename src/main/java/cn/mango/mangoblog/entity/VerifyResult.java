package cn.mango.mangoblog.entity;

public class VerifyResult {
    private final Long id;
    private final Integer privilege;

    public VerifyResult(Long id, Integer privilege) {
        this.id = id;
        this.privilege = privilege;
    }

    public Long getId() {
        return id;
    }

    public Integer getPrivilege() {
        return privilege;
    }
}
