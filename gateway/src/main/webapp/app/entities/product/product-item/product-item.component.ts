import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IProductItem } from '@/shared/model/product/product-item.model';

import ProductItemService from './product-item.service';
import AlertService from '@/shared/alert/alert.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class ProductItem extends Vue {
  @Inject('productItemService') private productItemService: () => ProductItemService;
  @Inject('alertService') private alertService: () => AlertService;

  private removeId: number = null;

  public productItems: IProductItem[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllProductItems();
  }

  public clear(): void {
    this.retrieveAllProductItems();
  }

  public retrieveAllProductItems(): void {
    this.isFetching = true;
    this.productItemService()
      .retrieve()
      .then(
        res => {
          this.productItems = res.data;
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

  public prepareRemove(instance: IProductItem): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeProductItem(): void {
    this.productItemService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('productApp.productProductItem.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllProductItems();
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
