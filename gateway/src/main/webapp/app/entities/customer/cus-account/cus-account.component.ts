import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { ICusAccount } from '@/shared/model/customer/cus-account.model';

import CusAccountService from './cus-account.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class CusAccount extends Vue {
  @Inject('cusAccountService') private cusAccountService: () => CusAccountService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public cusAccounts: ICusAccount[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllCusAccounts();
  }

  public clear(): void {
    this.retrieveAllCusAccounts();
  }

  public retrieveAllCusAccounts(): void {
    this.isFetching = true;
    this.cusAccountService()
      .retrieve()
      .then(
        res => {
          this.cusAccounts = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
          this.alertService().showHttpError(this, err.response);
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: ICusAccount): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeCusAccount(): void {
    this.cusAccountService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('customerApp.customerCusAccount.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllCusAccounts();
        this.closeDialog();
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
