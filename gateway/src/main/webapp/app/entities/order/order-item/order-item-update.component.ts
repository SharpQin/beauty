import { Component, Vue, Inject } from 'vue-property-decorator';

import { numeric, required, minValue, decimal } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import OrderService from '@/entities/order/order/order.service';
import { IOrder } from '@/shared/model/order/order.model';

import { IOrderItem, OrderItem } from '@/shared/model/order/order-item.model';
import OrderItemService from './order-item.service';
import { OrderItemStatus } from '@/shared/model/enumerations/order-item-status.model';

const validations: any = {
  orderItem: {
    prodId: {
      required,
      numeric,
    },
    quantity: {
      required,
      numeric,
      min: minValue(0),
    },
    totalPrice: {
      required,
      decimal,
      min: minValue(0),
    },
    price: {
      required,
      decimal,
      min: minValue(0),
    },
    status: {
      required,
    },
  },
};

@Component({
  validations,
})
export default class OrderItemUpdate extends Vue {
  @Inject('orderItemService') private orderItemService: () => OrderItemService;
  @Inject('alertService') private alertService: () => AlertService;

  public orderItem: IOrderItem = new OrderItem();

  @Inject('orderService') private orderService: () => OrderService;

  public orders: IOrder[] = [];
  public orderItemStatusValues: string[] = Object.keys(OrderItemStatus);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.orderItemId) {
        vm.retrieveOrderItem(to.params.orderItemId);
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
    if (this.orderItem.id) {
      this.orderItemService()
        .update(this.orderItem)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('orderApp.orderOrderItem.updated', { param: param.id });
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
      this.orderItemService()
        .create(this.orderItem)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('orderApp.orderOrderItem.created', { param: param.id });
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

  public retrieveOrderItem(orderItemId): void {
    this.orderItemService()
      .find(orderItemId)
      .then(res => {
        this.orderItem = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.orderService()
      .retrieve()
      .then(res => {
        this.orders = res.data;
      });
  }
}
