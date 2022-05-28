<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="gatewayApp.orderOrderItem.home.createOrEditLabel"
          data-cy="OrderItemCreateUpdateHeading"
          v-text="$t('gatewayApp.orderOrderItem.home.createOrEditLabel')"
        >
          Create or edit a OrderItem
        </h2>
        <div>
          <div class="form-group" v-if="orderItem.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="orderItem.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.orderOrderItem.prodId')" for="order-item-prodId">Prod Id</label>
            <input
              type="number"
              class="form-control"
              name="prodId"
              id="order-item-prodId"
              data-cy="prodId"
              :class="{ valid: !$v.orderItem.prodId.$invalid, invalid: $v.orderItem.prodId.$invalid }"
              v-model.number="$v.orderItem.prodId.$model"
              required
            />
            <div v-if="$v.orderItem.prodId.$anyDirty && $v.orderItem.prodId.$invalid">
              <small class="form-text text-danger" v-if="!$v.orderItem.prodId.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.orderItem.prodId.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.orderOrderItem.quantity')" for="order-item-quantity">Quantity</label>
            <input
              type="number"
              class="form-control"
              name="quantity"
              id="order-item-quantity"
              data-cy="quantity"
              :class="{ valid: !$v.orderItem.quantity.$invalid, invalid: $v.orderItem.quantity.$invalid }"
              v-model.number="$v.orderItem.quantity.$model"
              required
            />
            <div v-if="$v.orderItem.quantity.$anyDirty && $v.orderItem.quantity.$invalid">
              <small class="form-text text-danger" v-if="!$v.orderItem.quantity.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.orderItem.quantity.min" v-text="$t('entity.validation.min', { min: 0 })">
                This field should be at least 0.
              </small>
              <small class="form-text text-danger" v-if="!$v.orderItem.quantity.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.orderOrderItem.totalPrice')" for="order-item-totalPrice"
              >Total Price</label
            >
            <input
              type="number"
              class="form-control"
              name="totalPrice"
              id="order-item-totalPrice"
              data-cy="totalPrice"
              :class="{ valid: !$v.orderItem.totalPrice.$invalid, invalid: $v.orderItem.totalPrice.$invalid }"
              v-model.number="$v.orderItem.totalPrice.$model"
              required
            />
            <div v-if="$v.orderItem.totalPrice.$anyDirty && $v.orderItem.totalPrice.$invalid">
              <small class="form-text text-danger" v-if="!$v.orderItem.totalPrice.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.orderItem.totalPrice.min" v-text="$t('entity.validation.min', { min: 0 })">
                This field should be at least 0.
              </small>
              <small class="form-text text-danger" v-if="!$v.orderItem.totalPrice.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.orderOrderItem.price')" for="order-item-price">Price</label>
            <input
              type="number"
              class="form-control"
              name="price"
              id="order-item-price"
              data-cy="price"
              :class="{ valid: !$v.orderItem.price.$invalid, invalid: $v.orderItem.price.$invalid }"
              v-model.number="$v.orderItem.price.$model"
              required
            />
            <div v-if="$v.orderItem.price.$anyDirty && $v.orderItem.price.$invalid">
              <small class="form-text text-danger" v-if="!$v.orderItem.price.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.orderItem.price.min" v-text="$t('entity.validation.min', { min: 0 })">
                This field should be at least 0.
              </small>
              <small class="form-text text-danger" v-if="!$v.orderItem.price.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.orderOrderItem.status')" for="order-item-status">Status</label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !$v.orderItem.status.$invalid, invalid: $v.orderItem.status.$invalid }"
              v-model="$v.orderItem.status.$model"
              id="order-item-status"
              data-cy="status"
              required
            >
              <option
                v-for="orderItemStatus in orderItemStatusValues"
                :key="orderItemStatus"
                v-bind:value="orderItemStatus"
                v-bind:label="$t('gatewayApp.OrderItemStatus.' + orderItemStatus)"
              >
                {{ orderItemStatus }}
              </option>
            </select>
            <div v-if="$v.orderItem.status.$anyDirty && $v.orderItem.status.$invalid">
              <small class="form-text text-danger" v-if="!$v.orderItem.status.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.orderOrderItem.order')" for="order-item-order">Order</label>
            <select class="form-control" id="order-item-order" data-cy="order" name="order" v-model="orderItem.order">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="orderItem.order && orderOption.id === orderItem.order.id ? orderItem.order : orderOption"
                v-for="orderOption in orders"
                :key="orderOption.id"
              >
                {{ orderOption.id }}
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
            :disabled="$v.orderItem.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./order-item-update.component.ts"></script>
