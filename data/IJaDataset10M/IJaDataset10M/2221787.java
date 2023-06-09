package models;

import siena.Generator;
import siena.Id;
import siena.Model;
import siena.Query;
import siena.Table;
import siena.core.batch.Batch;

@Table("transaction_from")
public class TransactionAccountFromModel extends Model {

    @Id(Generator.AUTO_INCREMENT)
    public Long id;

    public Long amount;

    public TransactionAccountFromModel() {
    }

    public TransactionAccountFromModel(Long amount) {
        this.amount = amount;
    }

    public Query<TransactionAccountFromModel> all() {
        return Model.all(TransactionAccountFromModel.class);
    }

    public Batch<TransactionAccountFromModel> batch() {
        return Model.batch(TransactionAccountFromModel.class);
    }

    public String toString() {
        return "id: " + id + ", amount: " + amount;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        TransactionAccountFromModel other = (TransactionAccountFromModel) obj;
        if (amount == null) {
            if (other.amount != null) return false;
        }
        if (!amount.equals(other.amount)) return false;
        return true;
    }

    public boolean isOnlyIdFilled() {
        if (this.id != null && this.amount == null) return true;
        return false;
    }
}
