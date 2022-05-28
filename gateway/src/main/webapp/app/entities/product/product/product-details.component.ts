import { Component, Vue, Inject } from 'vue-property-decorator';

import { IProduct } from '@/shared/model/product/product.model';
import ProductService from './product.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ProductDetails extends Vue {
  @Inject('productService') private productService: () => ProductService;
  @Inject('alertService') private alertService: () => AlertService;

  public product: IProduct = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.productId) {
        vm.retrieveProduct(to.params.productId);
      }
    });
  }

  public retrieveProduct(productId) {
    this.productService()
      .find(productId)
      .then(res => {
        this.product = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
