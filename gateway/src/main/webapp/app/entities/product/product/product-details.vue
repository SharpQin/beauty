<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="product">
        <h2 class="jh-entity-heading" data-cy="productDetailsHeading">
          <span v-text="$t('gatewayApp.productProduct.detail.title')">Product</span> {{ product.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('gatewayApp.productProduct.name')">Name</span>
          </dt>
          <dd>
            <span>{{ product.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.productProduct.price')">Price</span>
          </dt>
          <dd>
            <span>{{ product.price }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.productProduct.type')">Type</span>
          </dt>
          <dd>
            <span v-text="$t('gatewayApp.ProductType.' + product.type)">{{ product.type }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.productProduct.image')">Image</span>
          </dt>
          <dd>
            <span>{{ product.image }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.productProduct.releaseDate')">Release Date</span>
          </dt>
          <dd>
            <span>{{ product.releaseDate }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.productProduct.liveTime')">Live Time</span>
          </dt>
          <dd>
            <span v-if="product.liveTime">{{ $d(Date.parse(product.liveTime), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.productProduct.createdTime')">Created Time</span>
          </dt>
          <dd>
            <span v-if="product.createdTime">{{ $d(Date.parse(product.createdTime), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.productProduct.updatedTime')">Updated Time</span>
          </dt>
          <dd>
            <span v-if="product.updatedTime">{{ $d(Date.parse(product.updatedTime), 'long') }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.productProduct.stock')">Stock</span>
          </dt>
          <dd>
            <span>{{ product.stock }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.productProduct.showed')">Showed</span>
          </dt>
          <dd>
            <span>{{ product.showed }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.productProduct.status')">Status</span>
          </dt>
          <dd>
            <span v-text="$t('gatewayApp.ProductStatus.' + product.status)">{{ product.status }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.productProduct.dsc')">Dsc</span>
          </dt>
          <dd>
            <span>{{ product.dsc }}</span>
          </dd>
          <dt>
            <span v-text="$t('gatewayApp.productProduct.category')">Category</span>
          </dt>
          <dd>
            <div v-if="product.category">
              <router-link :to="{ name: 'ProductCategoryView', params: { productCategoryId: product.category.id } }">{{
                product.category.name
              }}</router-link>
            </div>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="product.id" :to="{ name: 'ProductEdit', params: { productId: product.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./product-details.component.ts"></script>
