import { Component, Vue, Inject } from 'vue-property-decorator';

import { ICusAccount } from '@/shared/model/customer/cus-account.model';
import CusAccountService from './cus-account.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class CusAccountDetails extends Vue {
  @Inject('cusAccountService') private cusAccountService: () => CusAccountService;
  @Inject('alertService') private alertService: () => AlertService;

  public cusAccount: ICusAccount = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.cusAccountId) {
        vm.retrieveCusAccount(to.params.cusAccountId);
      }
    });
  }

  public retrieveCusAccount(cusAccountId) {
    this.cusAccountService()
      .find(cusAccountId)
      .then(res => {
        this.cusAccount = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
