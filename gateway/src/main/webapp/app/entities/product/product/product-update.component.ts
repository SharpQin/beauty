import { Component, Vue, Inject } from 'vue-property-decorator';

import { required, decimal } from 'vuelidate/lib/validators';
import dayjs from 'dayjs';
import { DATE_TIME_LONG_FORMAT } from '@/shared/date/filters';

import AlertService from '@/shared/alert/alert.service';

import ProductItemService from '@/entities/product/product-item/product-item.service';
import { IProductItem } from '@/shared/model/product/product-item.model';

import ProductCategoryService from '@/entities/product/product-category/product-category.service';
import { IProductCategory } from '@/shared/model/product/product-category.model';

import { IProduct, Product } from '@/shared/model/product/product.model';
import ProductService from './product.service';
import { ProductType } from '@/shared/model/enumerations/product-type.model';
import { ProductStatus } from '@/shared/model/enumerations/product-status.model';

const validations: any = {
  product: {
    name: {
      required,
    },
    price: {
      required,
      decimal,
    },
    type: {},
    image: {},
    releaseDate: {},
    liveTime: {},
    createdTime: {},
    updatedTime: {},
    stock: {},
    showed: {},
    status: {},
    dsc: {},
  },
};

@Component({
  validations,
})
export default class ProductUpdate extends Vue {
  @Inject('productService') private productService: () => ProductService;
  @Inject('alertService') private alertService: () => AlertService;

  public product: IProduct = new Product();

  @Inject('productItemService') private productItemService: () => ProductItemService;

  public productItems: IProductItem[] = [];

  @Inject('productCategoryService') private productCategoryService: () => ProductCategoryService;

  public productCategories: IProductCategory[] = [];
  public productTypeValues: string[] = Object.keys(ProductType);
  public productStatusValues: string[] = Object.keys(ProductStatus);
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.productId) {
        vm.retrieveProduct(to.params.productId);
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
    if (this.product.id) {
      this.productService()
        .update(this.product)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('productApp.productProduct.updated', { param: param.id });
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
      this.productService()
        .create(this.product)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('productApp.productProduct.created', { param: param.id });
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
      this.product[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.product[field] = null;
    }
  }

  public updateZonedDateTimeField(field, event) {
    if (event.target.value) {
      this.product[field] = dayjs(event.target.value, DATE_TIME_LONG_FORMAT);
    } else {
      this.product[field] = null;
    }
  }

  public retrieveProduct(productId): void {
    this.productService()
      .find(productId)
      .then(res => {
        res.liveTime = new Date(res.liveTime);
        res.createdTime = new Date(res.createdTime);
        res.updatedTime = new Date(res.updatedTime);
        this.product = res;
      })
      .catch(error => {
        this.alertService().showHttpError(this, error.response);
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.productItemService()
      .retrieve()
      .then(res => {
        this.productItems = res.data;
      });
    this.productCategoryService()
      .retrieve()
      .then(res => {
        this.productCategories = res.data;
      });
  }
}
