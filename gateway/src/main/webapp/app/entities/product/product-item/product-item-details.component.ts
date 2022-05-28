import { Component, Vue, Inject } from 'vue-property-decorator';

import { IProductItem } from '@/shared/model/product/product-item.model';
import ProductItemService from './product-item.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ProductItemDetails extends Vue {
  @Inject('productItemService') private productItemService: () => ProductItemService;
  @Inject('alertService') private alertService: () => AlertService;

  public productItem: IProductItem = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.productItemId) {
        vm.retrieveProductItem(to.params.productItemId);
      }
    });
  }

  public retrieveProductItem(productItemId) {
    this.productItemService()
      .find(productItemId)
      .then(res => {
        this.productItem = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
