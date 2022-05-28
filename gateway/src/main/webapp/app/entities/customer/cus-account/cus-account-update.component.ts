import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, maxLength } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import CustomerService from '@/entities/customer/customer/customer.service';
import { ICustomer } from '@/shared/model/customer/customer.model';

import { ICusAccount, CusAccount } from '@/shared/model/customer/cus-account.model';
import CusAccountService from './cus-account.service';
import { AccountType } from '@/shared/model/enumerations/account-type.model';

const validations: any = {
  cusAccount: {
    name: {
      required,
      maxLength: maxLength(20),
    },
    cardNo: {
      required,
      maxLength: maxLength(40),
    },
    accountType: {},
  },
};

@Component({
  validations,
})
export default class CusAccountUpdate extends Vue {
  @Inject('cusAccountService') private cusAccountService: () => CusAccountService;
  @Inject('alertService') private alertService: () => AlertService;

  public cusAccount: ICusAccount = new CusAccount();

  @Inject('customerService') private customerService: () => CustomerService;

  public customers: ICustomer[] = [];
  public accountTypeValues: string[] = Object.keys(AccountType);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.cusAccountId) {
        vm.retrieveCusAccount(to.params.cusAccountId);
      }
      vm.initRelationships();
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.cusAccount.id) {
      this.cusAccountService()
        .update(this.cusAccount)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('customerApp.customerCusAccount.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    } else {
      this.cusAccountService()
        .create(this.cusAccount)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('customerApp.customerCusAccount.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        })
        .catch(error => {
          this.isSaving = false;
          this.alertService().showHttpError(this, error.response);
        });
    }
  }

  public retrieveCusAccount(cusAccountId): void {
    this.cusAccountService()
      .find(cusAccountId)
      .then(res => {
        this.cusAccount = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.customerService()
      .retrieve()
      .then(res => {
        this.customers = res.data;
      });
  }
}
