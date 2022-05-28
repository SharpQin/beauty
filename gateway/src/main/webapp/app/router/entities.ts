import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const Address = () => import('@/entities/customer/address/address.vue');
const AddressUpdate = () => import('@/entities/customer/address/address-update.vue');
const AddressDetails = () => import('@/entities/customer/address/address-details.vue');

const CusAccount = () => import('@/entities/customer/cus-account/cus-account.vue');
const CusAccountUpdate = () => import('@/entities/customer/cus-account/cus-account-update.vue');
const CusAccountDetails = () => import('@/entities/customer/cus-account/cus-account-details.vue');

const Customer = () => import('@/entities/customer/customer/customer.vue');
const CustomerUpdate = () => import('@/entities/customer/customer/customer-update.vue');
const CustomerDetails = () => import('@/entities/customer/customer/customer-details.vue');

const Menu = () => import('@/entities/auth/menu/menu.vue');
const MenuUpdate = () => import('@/entities/auth/menu/menu-update.vue');
const MenuDetails = () => import('@/entities/auth/menu/menu-details.vue');

const Order = () => import('@/entities/order/order/order.vue');
const OrderUpdate = () => import('@/entities/order/order/order-update.vue');
const OrderDetails = () => import('@/entities/order/order/order-details.vue');

const OrderItem = () => import('@/entities/order/order-item/order-item.vue');
const OrderItemUpdate = () => import('@/entities/order/order-item/order-item-update.vue');
const OrderItemDetails = () => import('@/entities/order/order-item/order-item-details.vue');

const Permission = () => import('@/entities/auth/permission/permission.vue');
const PermissionUpdate = () => import('@/entities/auth/permission/permission-update.vue');
const PermissionDetails = () => import('@/entities/auth/permission/permission-details.vue');

const Product = () => import('@/entities/product/product/product.vue');
const ProductUpdate = () => import('@/entities/product/product/product-update.vue');
const ProductDetails = () => import('@/entities/product/product/product-details.vue');

const ProductCategory = () => import('@/entities/product/product-category/product-category.vue');
const ProductCategoryUpdate = () => import('@/entities/product/product-category/product-category-update.vue');
const ProductCategoryDetails = () => import('@/entities/product/product-category/product-category-details.vue');

const ProductItem = () => import('@/entities/product/product-item/product-item.vue');
const ProductItemUpdate = () => import('@/entities/product/product-item/product-item-update.vue');
const ProductItemDetails = () => import('@/entities/product/product-item/product-item-details.vue');

const Role = () => import('@/entities/auth/role/role.vue');
const RoleUpdate = () => import('@/entities/auth/role/role-update.vue');
const RoleDetails = () => import('@/entities/auth/role/role-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'address',
      name: 'Address',
      component: Address,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'address/new',
      name: 'AddressCreate',
      component: AddressUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'address/:addressId/edit',
      name: 'AddressEdit',
      component: AddressUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'address/:addressId/view',
      name: 'AddressView',
      component: AddressDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cus-account',
      name: 'CusAccount',
      component: CusAccount,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cus-account/new',
      name: 'CusAccountCreate',
      component: CusAccountUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cus-account/:cusAccountId/edit',
      name: 'CusAccountEdit',
      component: CusAccountUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'cus-account/:cusAccountId/view',
      name: 'CusAccountView',
      component: CusAccountDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'customer',
      name: 'Customer',
      component: Customer,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'customer/new',
      name: 'CustomerCreate',
      component: CustomerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'customer/:customerId/edit',
      name: 'CustomerEdit',
      component: CustomerUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'customer/:customerId/view',
      name: 'CustomerView',
      component: CustomerDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'menu',
      name: 'Menu',
      component: Menu,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'menu/new',
      name: 'MenuCreate',
      component: MenuUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'menu/:menuId/edit',
      name: 'MenuEdit',
      component: MenuUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'menu/:menuId/view',
      name: 'MenuView',
      component: MenuDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'order',
      name: 'Order',
      component: Order,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'order/new',
      name: 'OrderCreate',
      component: OrderUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'order/:orderId/edit',
      name: 'OrderEdit',
      component: OrderUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'order/:orderId/view',
      name: 'OrderView',
      component: OrderDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'order-item',
      name: 'OrderItem',
      component: OrderItem,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'order-item/new',
      name: 'OrderItemCreate',
      component: OrderItemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'order-item/:orderItemId/edit',
      name: 'OrderItemEdit',
      component: OrderItemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'order-item/:orderItemId/view',
      name: 'OrderItemView',
      component: OrderItemDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'permission',
      name: 'Permission',
      component: Permission,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'permission/new',
      name: 'PermissionCreate',
      component: PermissionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'permission/:permissionId/edit',
      name: 'PermissionEdit',
      component: PermissionUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'permission/:permissionId/view',
      name: 'PermissionView',
      component: PermissionDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product',
      name: 'Product',
      component: Product,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product/new',
      name: 'ProductCreate',
      component: ProductUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product/:productId/edit',
      name: 'ProductEdit',
      component: ProductUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product/:productId/view',
      name: 'ProductView',
      component: ProductDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product-category',
      name: 'ProductCategory',
      component: ProductCategory,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product-category/new',
      name: 'ProductCategoryCreate',
      component: ProductCategoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product-category/:productCategoryId/edit',
      name: 'ProductCategoryEdit',
      component: ProductCategoryUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product-category/:productCategoryId/view',
      name: 'ProductCategoryView',
      component: ProductCategoryDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product-item',
      name: 'ProductItem',
      component: ProductItem,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product-item/new',
      name: 'ProductItemCreate',
      component: ProductItemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product-item/:productItemId/edit',
      name: 'ProductItemEdit',
      component: ProductItemUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'product-item/:productItemId/view',
      name: 'ProductItemView',
      component: ProductItemDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'role',
      name: 'Role',
      component: Role,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'role/new',
      name: 'RoleCreate',
      component: RoleUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'role/:roleId/edit',
      name: 'RoleEdit',
      component: RoleUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'role/:roleId/view',
      name: 'RoleView',
      component: RoleDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
