package mm.wallet.domain.core;

public abstract class DomainRuleException extends IllegalStateException {

    public DomainRuleException(String s) {
        super(s);
    }
}
