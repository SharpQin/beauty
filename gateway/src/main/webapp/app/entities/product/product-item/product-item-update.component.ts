import { Component, Vue, Inject } from 'vue-property-decorator';

import AlertService from '@/shared/alert/alert.service';

import ProductService from '@/entities/product/product/product.service';
import { IProduct } from '@/shared/model/product/product.model';

import { IProductItem, ProductItem } from '@/shared/model/product/product-item.model';
import ProductItemService from './product-item.service';

const validations: any = {
  productItem: {
    name: {},
    amount: {},
    image: {},
    dsc: {},
  },
};

@Component({
  validations,
})
export default class ProductItemUpdate extends Vue {
  @Inject('productItemService') private productItemService: () => ProductItemService;
  @Inject('alertService') private alertService: () => AlertService;

  public productItem: IProductItem = new ProductItem();

  @Inject('productService') private productService: () => ProductService;

  public products: IProduct[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.productItemId) {
        vm.retrieveProductItem(to.params.productItemId);
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
    if (this.productItem.id) {
      this.productItemService()
        .update(this.productItem)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('productApp.productProductItem.updated', { param: param.id });
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
      this.productItemService()
        .create(this.productItem)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('productApp.productProductItem.created', { param: param.id });
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

  public retrieveProductItem(productItemId): void {
    this.productItemService()
      .find(productItemId)
      .then(res => {
        this.productItem = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.productService()
      .retrieve()
      .then(res => {
        this.products = res.data;
      });
  }
}
