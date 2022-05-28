import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IOrderItem } from '@/shared/model/order/order-item.model';

import OrderItemService from './order-item.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class OrderItem extends Vue {
  @Inject('orderItemService') private orderItemService: () => OrderItemService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public orderItems: IOrderItem[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllOrderItems();
  }

  public clear(): void {
    this.retrieveAllOrderItems();
  }

  public retrieveAllOrderItems(): void {
    this.isFetching = true;
    this.orderItemService()
      .retrieve()
      .then(
        res => {
          this.orderItems = res.data;
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

  public prepareRemove(instance: IOrderItem): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeOrderItem(): void {
    this.orderItemService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('orderApp.orderOrderItem.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllOrderItems();
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
