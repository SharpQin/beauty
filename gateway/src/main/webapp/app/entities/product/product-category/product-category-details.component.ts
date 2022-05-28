import { Component, Vue, Inject } from 'vue-property-decorator';

import { IProductCategory } from '@/shared/model/product/product-category.model';
import ProductCategoryService from './product-category.service';
import AlertService from '@/shared/alert/alert.service';

@Component
export default class ProductCategoryDetails extends Vue {
  @Inject('productCategoryService') private productCategoryService: () => ProductCategoryService;
  @Inject('alertService') private alertService: () => AlertService;

  public productCategory: IProductCategory = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.productCategoryId) {
        vm.retrieveProductCategory(to.params.productCategoryId);
      }
    });
  }

  public retrieveProductCategory(productCategoryId) {
    this.productCategoryService()
      .find(productCategoryId)
      .then(res => {
        this.productCategory = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
