<template>
  <div>
    <b-dropdown-item to="/address">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.customerAddress')">Address</span>
    </b-dropdown-item>
    <b-dropdown-item to="/cus-account">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.customerCusAccount')">Cus Account</span>
    </b-dropdown-item>
    <b-dropdown-item to="/customer">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.customerCustomer')">Customer</span>
    </b-dropdown-item>
    <b-dropdown-item to="/menu">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.authMenu')">Menu</span>
    </b-dropdown-item>
    <b-dropdown-item to="/order">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.orderOrder')">Order</span>
    </b-dropdown-item>
    <b-dropdown-item to="/order-item">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.orderOrderItem')">Order Item</span>
    </b-dropdown-item>
    <b-dropdown-item to="/permission">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.authPermission')">Permission</span>
    </b-dropdown-item>
    <b-dropdown-item to="/product">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.productProduct')">Product</span>
    </b-dropdown-item>
    <b-dropdown-item to="/product-category">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.productProductCategory')">Product Category</span>
    </b-dropdown-item>
    <b-dropdown-item to="/product-item">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.productProductItem')">Product Item</span>
    </b-dropdown-item>
    <b-dropdown-item to="/role">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.authRole')">Role</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
