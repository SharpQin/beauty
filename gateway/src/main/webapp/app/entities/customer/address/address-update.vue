<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="gatewayApp.customerAddress.home.createOrEditLabel"
          data-cy="AddressCreateUpdateHeading"
          v-text="$t('gatewayApp.customerAddress.home.createOrEditLabel')"
        >
          Create or edit a Address
        </h2>
        <div>
          <div class="form-group" v-if="address.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="address.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerAddress.postalCode')" for="address-postalCode"
              >Postal Code</label
            >
            <input
              type="text"
              class="form-control"
              name="postalCode"
              id="address-postalCode"
              data-cy="postalCode"
              :class="{ valid: !$v.address.postalCode.$invalid, invalid: $v.address.postalCode.$invalid }"
              v-model="$v.address.postalCode.$model"
            />
            <div v-if="$v.address.postalCode.$anyDirty && $v.address.postalCode.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.address.postalCode.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 10 })"
              >
                This field cannot be longer than 10 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerAddress.city')" for="address-city">City</label>
            <input
              type="text"
              class="form-control"
              name="city"
              id="address-city"
              data-cy="city"
              :class="{ valid: !$v.address.city.$invalid, invalid: $v.address.city.$invalid }"
              v-model="$v.address.city.$model"
            />
            <div v-if="$v.address.city.$anyDirty && $v.address.city.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.address.city.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 30 })"
              >
                This field cannot be longer than 30 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerAddress.details')" for="address-details">Details</label>
            <input
              type="text"
              class="form-control"
              name="details"
              id="address-details"
              data-cy="details"
              :class="{ valid: !$v.address.details.$invalid, invalid: $v.address.details.$invalid }"
              v-model="$v.address.details.$model"
              required
            />
            <div v-if="$v.address.details.$anyDirty && $v.address.details.$invalid">
              <small class="form-text text-danger" v-if="!$v.address.details.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.address.details.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 120 })"
              >
                This field cannot be longer than 120 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerAddress.defaultAddr')" for="address-defaultAddr"
              >Default Addr</label
            >
            <input
              type="checkbox"
              class="form-check"
              name="defaultAddr"
              id="address-defaultAddr"
              data-cy="defaultAddr"
              :class="{ valid: !$v.address.defaultAddr.$invalid, invalid: $v.address.defaultAddr.$invalid }"
              v-model="$v.address.defaultAddr.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerAddress.customer')" for="address-customer">Customer</label>
            <select class="form-control" id="address-customer" data-cy="customer" name="customer" v-model="address.customer">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="address.customer && customerOption.id === address.customer.id ? address.customer : customerOption"
                v-for="customerOption in customers"
                :key="customerOption.id"
              >
                {{ customerOption.id }}
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
            :disabled="$v.address.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./address-update.component.ts"></script>
