<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="gatewayApp.productProductItem.home.createOrEditLabel"
          data-cy="ProductItemCreateUpdateHeading"
          v-text="$t('gatewayApp.productProductItem.home.createOrEditLabel')"
        >
          Create or edit a ProductItem
        </h2>
        <div>
          <div class="form-group" v-if="productItem.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="productItem.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProductItem.name')" for="product-item-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="product-item-name"
              data-cy="name"
              :class="{ valid: !$v.productItem.name.$invalid, invalid: $v.productItem.name.$invalid }"
              v-model="$v.productItem.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProductItem.amount')" for="product-item-amount">Amount</label>
            <input
              type="number"
              class="form-control"
              name="amount"
              id="product-item-amount"
              data-cy="amount"
              :class="{ valid: !$v.productItem.amount.$invalid, invalid: $v.productItem.amount.$invalid }"
              v-model.number="$v.productItem.amount.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProductItem.image')" for="product-item-image">Image</label>
            <input
              type="text"
              class="form-control"
              name="image"
              id="product-item-image"
              data-cy="image"
              :class="{ valid: !$v.productItem.image.$invalid, invalid: $v.productItem.image.$invalid }"
              v-model="$v.productItem.image.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProductItem.dsc')" for="product-item-dsc">Dsc</label>
            <input
              type="text"
              class="form-control"
              name="dsc"
              id="product-item-dsc"
              data-cy="dsc"
              :class="{ valid: !$v.productItem.dsc.$invalid, invalid: $v.productItem.dsc.$invalid }"
              v-model="$v.productItem.dsc.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProductItem.product')" for="product-item-product"
              >Product</label
            >
            <select class="form-control" id="product-item-product" data-cy="product" name="product" v-model="productItem.product">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="productItem.product && productOption.id === productItem.product.id ? productItem.product : productOption"
                v-for="productOption in products"
                :key="productOption.id"
              >
                {{ productOption.id }}
              </option>
            </select>
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" data-cy="entityCreateCancelButton" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.productItem.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./product-item-update.component.ts"></script>
