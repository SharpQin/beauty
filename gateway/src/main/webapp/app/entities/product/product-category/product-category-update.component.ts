import { Component, Vue, Inject } from 'vue-property-decorator';

import { required } from 'vuelidate/lib/validators';

import AlertService from '@/shared/alert/alert.service';

import ProductService from '@/entities/product/product/product.service';
import { IProduct } from '@/shared/model/product/product.model';

import { IProductCategory, ProductCategory } from '@/shared/model/product/product-category.model';
import ProductCategoryService from './product-category.service';

const validations: any = {
  productCategory: {
    name: {
      required,
    },
    dsc: {},
  },
};

@Component({
  validations,
})
export default class ProductCategoryUpdate extends Vue {
  @Inject('productCategoryService') private productCategoryService: () => ProductCategoryService;
  @Inject('alertService') private alertService: () => AlertService;

  public productCategory: IProductCategory = new ProductCategory();

  @Inject('productService') private productService: () => ProductService;

  public products: IProduct[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.productCategoryId) {
        vm.retrieveProductCategory(to.params.productCategoryId);
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
    if (this.productCategory.id) {
      this.productCategoryService()
        .update(this.productCategory)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('productApp.productProductCategory.updated', { param: param.id });
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
      this.productCategoryService()
        .create(this.productCategory)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('productApp.productProductCategory.created', { param: param.id });
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

  public retrieveProductCategory(productCategoryId): void {
    this.productCategoryService()
      .find(productCategoryId)
      .then(res => {
        this.productCategory = res;
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
