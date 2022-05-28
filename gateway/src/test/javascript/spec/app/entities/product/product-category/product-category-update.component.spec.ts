/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';
import { ToastPlugin } from 'bootstrap-vue';

import * as config from '@/shared/config/config';
import ProductCategoryUpdateComponent from '@/entities/product/product-category/product-category-update.vue';
import ProductCategoryClass from '@/entities/product/product-category/product-category-update.component';
import ProductCategoryService from '@/entities/product/product-category/product-category.service';

import ProductService from '@/entities/product/product/product.service';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.use(ToastPlugin);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('ProductCategory Management Update Component', () => {
    let wrapper: Wrapper<ProductCategoryClass>;
    let comp: ProductCategoryClass;
    let productCategoryServiceStub: SinonStubbedInstance<ProductCategoryService>;

    beforeEach(() => {
      productCategoryServiceStub = sinon.createStubInstance<ProductCategoryService>(ProductCategoryService);

      wrapper = shallowMount<ProductCategoryClass>(ProductCategoryUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          productCategoryService: () => productCategoryServiceStub,
          alertService: () => new AlertService(),

          productService: () =>
            sinon.createStubInstance<ProductService>(ProductService, {
              retrieve: sinon.stub().resolves({}),
            } as any),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.productCategory = entity;
        productCategoryServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(productCategoryServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.productCategory = entity;
        productCategoryServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(productCategoryServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProductCategory = { id: 123 };
        productCategoryServiceStub.find.resolves(foundProductCategory);
        productCategoryServiceStub.retrieve.resolves([foundProductCategory]);

        // WHEN
        comp.beforeRouteEnter({ params: { productCategoryId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.productCategory).toBe(foundProductCategory);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
