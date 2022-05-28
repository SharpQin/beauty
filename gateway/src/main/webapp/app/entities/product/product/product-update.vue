<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="gatewayApp.productProduct.home.createOrEditLabel"
          data-cy="ProductCreateUpdateHeading"
          v-text="$t('gatewayApp.productProduct.home.createOrEditLabel')"
        >
          Create or edit a Product
        </h2>
        <div>
          <div class="form-group" v-if="product.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="product.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProduct.name')" for="product-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="product-name"
              data-cy="name"
              :class="{ valid: !$v.product.name.$invalid, invalid: $v.product.name.$invalid }"
              v-model="$v.product.name.$model"
              required
            />
            <div v-if="$v.product.name.$anyDirty && $v.product.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.product.name.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProduct.price')" for="product-price">Price</label>
            <input
              type="number"
              class="form-control"
              name="price"
              id="product-price"
              data-cy="price"
              :class="{ valid: !$v.product.price.$invalid, invalid: $v.product.price.$invalid }"
              v-model.number="$v.product.price.$model"
              required
            />
            <div v-if="$v.product.price.$anyDirty && $v.product.price.$invalid">
              <small class="form-text text-danger" v-if="!$v.product.price.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.product.price.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProduct.type')" for="product-type">Type</label>
            <select
              class="form-control"
              name="type"
              :class="{ valid: !$v.product.type.$invalid, invalid: $v.product.type.$invalid }"
              v-model="$v.product.type.$model"
              id="product-type"
              data-cy="type"
            >
              <option
                v-for="productType in productTypeValues"
                :key="productType"
                v-bind:value="productType"
                v-bind:label="$t('gatewayApp.ProductType.' + productType)"
              >
                {{ productType }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProduct.image')" for="product-image">Image</label>
            <input
              type="text"
              class="form-control"
              name="image"
              id="product-image"
              data-cy="image"
              :class="{ valid: !$v.product.image.$invalid, invalid: $v.product.image.$invalid }"
              v-model="$v.product.image.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProduct.releaseDate')" for="product-releaseDate"
              >Release Date</label
            >
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="product-releaseDate"
                  v-model="$v.product.releaseDate.$model"
                  name="releaseDate"
                  class="form-control"
                  :locale="currentLanguage"
                  button-only
                  today-button
                  reset-button
                  close-button
                >
                </b-form-datepicker>
              </b-input-group-prepend>
              <b-form-input
                id="product-releaseDate"
                data-cy="releaseDate"
                type="text"
                class="form-control"
                name="releaseDate"
                :class="{ valid: !$v.product.releaseDate.$invalid, invalid: $v.product.releaseDate.$invalid }"
                v-model="$v.product.releaseDate.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProduct.liveTime')" for="product-liveTime">Live Time</label>
            <div class="d-flex">
              <input
                id="product-liveTime"
                data-cy="liveTime"
                type="datetime-local"
                class="form-control"
                name="liveTime"
                :class="{ valid: !$v.product.liveTime.$invalid, invalid: $v.product.liveTime.$invalid }"
                :value="convertDateTimeFromServer($v.product.liveTime.$model)"
                @change="updateInstantField('liveTime', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProduct.createdTime')" for="product-createdTime"
              >Created Time</label
            >
            <div class="d-flex">
              <input
                id="product-createdTime"
                data-cy="createdTime"
                type="datetime-local"
                class="form-control"
                name="createdTime"
                :class="{ valid: !$v.product.createdTime.$invalid, invalid: $v.product.createdTime.$invalid }"
                :value="convertDateTimeFromServer($v.product.createdTime.$model)"
                @change="updateInstantField('createdTime', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProduct.updatedTime')" for="product-updatedTime"
              >Updated Time</label
            >
            <div class="d-flex">
              <input
                id="product-updatedTime"
                data-cy="updatedTime"
                type="datetime-local"
                class="form-control"
                name="updatedTime"
                :class="{ valid: !$v.product.updatedTime.$invalid, invalid: $v.product.updatedTime.$invalid }"
                :value="convertDateTimeFromServer($v.product.updatedTime.$model)"
                @change="updateInstantField('updatedTime', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProduct.stock')" for="product-stock">Stock</label>
            <input
              type="number"
              class="form-control"
              name="stock"
              id="product-stock"
              data-cy="stock"
              :class="{ valid: !$v.product.stock.$invalid, invalid: $v.product.stock.$invalid }"
              v-model.number="$v.product.stock.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProduct.showed')" for="product-showed">Showed</label>
            <input
              type="checkbox"
              class="form-check"
              name="showed"
              id="product-showed"
              data-cy="showed"
              :class="{ valid: !$v.product.showed.$invalid, invalid: $v.product.showed.$invalid }"
              v-model="$v.product.showed.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProduct.status')" for="product-status">Status</label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !$v.product.status.$invalid, invalid: $v.product.status.$invalid }"
              v-model="$v.product.status.$model"
              id="product-status"
              data-cy="status"
            >
              <option
                v-for="productStatus in productStatusValues"
                :key="productStatus"
                v-bind:value="productStatus"
                v-bind:label="$t('gatewayApp.ProductStatus.' + productStatus)"
              >
                {{ productStatus }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProduct.dsc')" for="product-dsc">Dsc</label>
            <input
              type="text"
              class="form-control"
              name="dsc"
              id="product-dsc"
              data-cy="dsc"
              :class="{ valid: !$v.product.dsc.$invalid, invalid: $v.product.dsc.$invalid }"
              v-model="$v.product.dsc.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.productProduct.category')" for="product-category">Category</label>
            <select class="form-control" id="product-category" data-cy="category" name="category" v-model="product.category">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="
                  product.category && productCategoryOption.id === product.category.id ? product.category : productCategoryOption
                "
                v-for="productCategoryOption in productCategories"
                :key="productCategoryOption.id"
              >
                {{ productCategoryOption.name }}
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
            :disabled="$v.product.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./product-update.component.ts"></script>
