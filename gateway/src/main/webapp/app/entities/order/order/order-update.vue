<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="gatewayApp.orderOrder.home.createOrEditLabel"
          data-cy="OrderCreateUpdateHeading"
          v-text="$t('gatewayApp.orderOrder.home.createOrEditLabel')"
        >
          Create or edit a Order
        </h2>
        <div>
          <div class="form-group" v-if="order.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="order.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.orderOrder.serialNo')" for="order-serialNo">Serial No</label>
            <input
              type="text"
              class="form-control"
              name="serialNo"
              id="order-serialNo"
              data-cy="serialNo"
              :class="{ valid: !$v.order.serialNo.$invalid, invalid: $v.order.serialNo.$invalid }"
              v-model="$v.order.serialNo.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.orderOrder.price')" for="order-price">Price</label>
            <input
              type="number"
              class="form-control"
              name="price"
              id="order-price"
              data-cy="price"
              :class="{ valid: !$v.order.price.$invalid, invalid: $v.order.price.$invalid }"
              v-model.number="$v.order.price.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.orderOrder.status')" for="order-status">Status</label>
            <select
              class="form-control"
              name="status"
              :class="{ valid: !$v.order.status.$invalid, invalid: $v.order.status.$invalid }"
              v-model="$v.order.status.$model"
              id="order-status"
              data-cy="status"
            >
              <option
                v-for="orderStatus in orderStatusValues"
                :key="orderStatus"
                v-bind:value="orderStatus"
                v-bind:label="$t('gatewayApp.OrderStatus.' + orderStatus)"
              >
                {{ orderStatus }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.orderOrder.custId')" for="order-custId">Cust Id</label>
            <input
              type="number"
              class="form-control"
              name="custId"
              id="order-custId"
              data-cy="custId"
              :class="{ valid: !$v.order.custId.$invalid, invalid: $v.order.custId.$invalid }"
              v-model.number="$v.order.custId.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.orderOrder.createdTime')" for="order-createdTime">Created Time</label>
            <div class="d-flex">
              <input
                id="order-createdTime"
                data-cy="createdTime"
                type="datetime-local"
                class="form-control"
                name="createdTime"
                :class="{ valid: !$v.order.createdTime.$invalid, invalid: $v.order.createdTime.$invalid }"
                :value="convertDateTimeFromServer($v.order.createdTime.$model)"
                @change="updateInstantField('createdTime', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.orderOrder.actionTime')" for="order-actionTime">Action Time</label>
            <div class="d-flex">
              <input
                id="order-actionTime"
                data-cy="actionTime"
                type="datetime-local"
                class="form-control"
                name="actionTime"
                :class="{ valid: !$v.order.actionTime.$invalid, invalid: $v.order.actionTime.$invalid }"
                :value="convertDateTimeFromServer($v.order.actionTime.$model)"
                @change="updateInstantField('actionTime', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.orderOrder.remark')" for="order-remark">Remark</label>
            <input
              type="text"
              class="form-control"
              name="remark"
              id="order-remark"
              data-cy="remark"
              :class="{ valid: !$v.order.remark.$invalid, invalid: $v.order.remark.$invalid }"
              v-model="$v.order.remark.$model"
            />
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
            :disabled="$v.order.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./order-update.component.ts"></script>
