<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="productItem">
        <h2 class="jh-entity-heading" data-cy="productItemDetailsHeading">
          <span v-text="$t('gatewayApp.productProductItem.detail.title')">ProductItem</span> {{ productItem.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('gatewayApp.productProductItem.name')">Name</span>
          </dt>
          <dd>
            <span>{{ productItem.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.productProductItem.amount')">Amount</span>
          </dt>
          <dd>
            <span>{{ productItem.amount }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.productProductItem.image')">Image</span>
          </dt>
          <dd>
            <span>{{ productItem.image }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.productProductItem.dsc')">Dsc</span>
          </dt>
          <dd>
            <span>{{ productItem.dsc }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.productProductItem.product')">Product</span>
          </dt>
          <dd>
            <div v-if="productItem.product">
              <router-link :to="{ name: 'ProductView', params: { productId: productItem.product.id } }">{{
                productItem.product.id
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="productItem.id"
          :to="{ name: 'ProductItemEdit', params: { productItemId: productItem.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./product-item-details.component.ts"></script>
