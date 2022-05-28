import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, maxLength, minLength, numeric } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import AddressService from '@/entities/customer/address/address.service';
import { IAddress } from '@/shared/model/customer/address.model';

import CusAccountService from '@/entities/customer/cus-account/cus-account.service';
import { ICusAccount } from '@/shared/model/customer/cus-account.model';

import { ICustomer, Customer } from '@/shared/model/customer/customer.model';
import CustomerService from './customer.service';

const validations: any = {
  customer: {
    name: {
      required,
      maxLength: maxLength(50),
    },
    phone: {
      maxLength: maxLength(15),
    },
    email: {
      maxLength: maxLength(20),
    },
    birthday: {},
    imageUrl: {
      maxLength: maxLength(50),
    },
    langKey: {
      minLength: minLength(2),
      maxLength: maxLength(10),
    },
    vip: {
      required,
      numeric,
    },
    level: {
      required,
      numeric,
    },
    point: {
      required,
      numeric,
    },
    activated: {},
    createdDate: {},
    lastModifiedDate: {},
  },
};

@Component({
  validations,
})
export default class CustomerUpdate extends Vue {
  @Inject('customerService') private customerService: () => CustomerService;
  @Inject('alertService') private alertService: () => AlertService;

  public customer: ICustomer = new Customer();

  @Inject('addressService') private addressService: () => AddressService;

  public addresses: IAddress[] = [];

  @Inject('cusAccountService') private cusAccountService: () => CusAccountService;

  public cusAccounts: ICusAccount[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.customerId) {
        vm.retrieveCustomer(to.params.customerId);
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
    if (this.customer.id) {
      this.customerService()
        .update(this.customer)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('customerApp.customerCustomer.updated', { param: param.id });
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
      this.customerService()
        .create(this.customer)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('customerApp.customerCustomer.created', { param: param.id });
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

  public convertDateTimeFromServer(date: Date): string {
    if (date && dayjs(date).isValid()) {
      return dayjs(date).format(DATE_TIME_LONG_FORMAT);
    }
    return null;
  }

  public updateInstantField(field, event) {
    if (event.target.value) {
      this.customer[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.customer[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.customer[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.customer[field] = null;
    }
  }

  public retrieveCustomer(customerId): void {
    this.customerService()
      .find(customerId)
      .then(res => {
        res.createdDate = new Date(res.createdDate);
        res.lastModifiedDate = new Date(res.lastModifiedDate);
        this.customer = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.addressService()
      .retrieve()
      .then(res => {
        this.addresses = res.data;
      });
    this.cusAccountService()
      .retrieve()
      .then(res => {
        this.cusAccounts = res.data;
      });
  }
}
