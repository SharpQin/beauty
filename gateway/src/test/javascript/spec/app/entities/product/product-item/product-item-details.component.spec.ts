/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ProductItemDetailComponent from '@/entities/product/product-item/product-item-details.vue';
import ProductItemClass from '@/entities/product/product-item/product-item-details.component';
import ProductItemService from '@/entities/product/product-item/product-item.service';
import router from '@/router';
import AlertService from '@/shared/alert/alert.service';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('ProductItem Management Detail Component', () => {
    let wrapper: Wrapper<ProductItemClass>;
    let comp: ProductItemClass;
    let productItemServiceStub: SinonStubbedInstance<ProductItemService>;

    beforeEach(() => {
      productItemServiceStub = sinon.createStubInstance<ProductItemService>(ProductItemService);

      wrapper = shallowMount<ProductItemClass>(ProductItemDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { productItemService: () => productItemServiceStub, alertService: () => new AlertService() },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundProductItem = { id: 123 };
        productItemServiceStub.find.resolves(foundProductItem);

        // WHEN
        comp.retrieveProductItem(123);
        await comp.$nextTick();

        // THEN
        expect(comp.productItem).toBe(foundProductItem);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundProductItem = { id: 123 };
        productItemServiceStub.find.resolves(foundProductItem);

        // WHEN
        comp.beforeRouteEnter({ params: { productItemId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.productItem).toBe(foundProductItem);
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
