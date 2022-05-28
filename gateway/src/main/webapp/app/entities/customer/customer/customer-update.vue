<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="gatewayApp.customerCustomer.home.createOrEditLabel"
          data-cy="CustomerCreateUpdateHeading"
          v-text="$t('gatewayApp.customerCustomer.home.createOrEditLabel')"
        >
          Create or edit a Customer
        </h2>
        <div>
          <div class="form-group" v-if="customer.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="customer.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerCustomer.name')" for="customer-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="customer-name"
              data-cy="name"
              :class="{ valid: !$v.customer.name.$invalid, invalid: $v.customer.name.$invalid }"
              v-model="$v.customer.name.$model"
              required
            />
            <div v-if="$v.customer.name.$anyDirty && $v.customer.name.$invalid">
              <small class="form-text text-danger" v-if="!$v.customer.name.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.customer.name.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 50 })"
              >
                This field cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerCustomer.phone')" for="customer-phone">Phone</label>
            <input
              type="text"
              class="form-control"
              name="phone"
              id="customer-phone"
              data-cy="phone"
              :class="{ valid: !$v.customer.phone.$invalid, invalid: $v.customer.phone.$invalid }"
              v-model="$v.customer.phone.$model"
            />
            <div v-if="$v.customer.phone.$anyDirty && $v.customer.phone.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.customer.phone.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 15 })"
              >
                This field cannot be longer than 15 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerCustomer.email')" for="customer-email">Email</label>
            <input
              type="text"
              class="form-control"
              name="email"
              id="customer-email"
              data-cy="email"
              :class="{ valid: !$v.customer.email.$invalid, invalid: $v.customer.email.$invalid }"
              v-model="$v.customer.email.$model"
            />
            <div v-if="$v.customer.email.$anyDirty && $v.customer.email.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.customer.email.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 20 })"
              >
                This field cannot be longer than 20 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerCustomer.birthday')" for="customer-birthday">Birthday</label>
            <b-input-group class="mb-3">
              <b-input-group-prepend>
                <b-form-datepicker
                  aria-controls="customer-birthday"
                  v-model="$v.customer.birthday.$model"
                  name="birthday"
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
                id="customer-birthday"
                data-cy="birthday"
                type="text"
                class="form-control"
                name="birthday"
                :class="{ valid: !$v.customer.birthday.$invalid, invalid: $v.customer.birthday.$invalid }"
                v-model="$v.customer.birthday.$model"
              />
            </b-input-group>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerCustomer.imageUrl')" for="customer-imageUrl">Image Url</label>
            <input
              type="text"
              class="form-control"
              name="imageUrl"
              id="customer-imageUrl"
              data-cy="imageUrl"
              :class="{ valid: !$v.customer.imageUrl.$invalid, invalid: $v.customer.imageUrl.$invalid }"
              v-model="$v.customer.imageUrl.$model"
            />
            <div v-if="$v.customer.imageUrl.$anyDirty && $v.customer.imageUrl.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.customer.imageUrl.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 50 })"
              >
                This field cannot be longer than 50 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerCustomer.langKey')" for="customer-langKey">Lang Key</label>
            <input
              type="text"
              class="form-control"
              name="langKey"
              id="customer-langKey"
              data-cy="langKey"
              :class="{ valid: !$v.customer.langKey.$invalid, invalid: $v.customer.langKey.$invalid }"
              v-model="$v.customer.langKey.$model"
            />
            <div v-if="$v.customer.langKey.$anyDirty && $v.customer.langKey.$invalid">
              <small
                class="form-text text-danger"
                v-if="!$v.customer.langKey.minLength"
                v-text="$t('entity.validation.minlength', { min: 2 })"
              >
                This field is required to be at least 2 characters.
              </small>
              <small
                class="form-text text-danger"
                v-if="!$v.customer.langKey.maxLength"
                v-text="$t('entity.validation.maxlength', { max: 10 })"
              >
                This field cannot be longer than 10 characters.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerCustomer.vip')" for="customer-vip">Vip</label>
            <input
              type="number"
              class="form-control"
              name="vip"
              id="customer-vip"
              data-cy="vip"
              :class="{ valid: !$v.customer.vip.$invalid, invalid: $v.customer.vip.$invalid }"
              v-model.number="$v.customer.vip.$model"
              required
            />
            <div v-if="$v.customer.vip.$anyDirty && $v.customer.vip.$invalid">
              <small class="form-text text-danger" v-if="!$v.customer.vip.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.customer.vip.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerCustomer.level')" for="customer-level">Level</label>
            <input
              type="number"
              class="form-control"
              name="level"
              id="customer-level"
              data-cy="level"
              :class="{ valid: !$v.customer.level.$invalid, invalid: $v.customer.level.$invalid }"
              v-model.number="$v.customer.level.$model"
              required
            />
            <div v-if="$v.customer.level.$anyDirty && $v.customer.level.$invalid">
              <small class="form-text text-danger" v-if="!$v.customer.level.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.customer.level.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerCustomer.point')" for="customer-point">Point</label>
            <input
              type="number"
              class="form-control"
              name="point"
              id="customer-point"
              data-cy="point"
              :class="{ valid: !$v.customer.point.$invalid, invalid: $v.customer.point.$invalid }"
              v-model.number="$v.customer.point.$model"
              required
            />
            <div v-if="$v.customer.point.$anyDirty && $v.customer.point.$invalid">
              <small class="form-text text-danger" v-if="!$v.customer.point.required" v-text="$t('entity.validation.required')">
                This field is required.
              </small>
              <small class="form-text text-danger" v-if="!$v.customer.point.numeric" v-text="$t('entity.validation.number')">
                This field should be a number.
              </small>
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerCustomer.activated')" for="customer-activated"
              >Activated</label
            >
            <input
              type="checkbox"
              class="form-check"
              name="activated"
              id="customer-activated"
              data-cy="activated"
              :class="{ valid: !$v.customer.activated.$invalid, invalid: $v.customer.activated.$invalid }"
              v-model="$v.customer.activated.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerCustomer.createdDate')" for="customer-createdDate"
              >Created Date</label
            >
            <div class="d-flex">
              <input
                id="customer-createdDate"
                data-cy="createdDate"
                type="datetime-local"
                class="form-control"
                name="createdDate"
                :class="{ valid: !$v.customer.createdDate.$invalid, invalid: $v.customer.createdDate.$invalid }"
                :value="convertDateTimeFromServer($v.customer.createdDate.$model)"
                @change="updateInstantField('createdDate', $event)"
              />
            </div>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('gatewayApp.customerCustomer.lastModifiedDate')" for="customer-lastModifiedDate"
              >Last Modified Date</label
            >
            <div class="d-flex">
              <input
                id="customer-lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                class="form-control"
                name="lastModifiedDate"
                :class="{ valid: !$v.customer.lastModifiedDate.$invalid, invalid: $v.customer.lastModifiedDate.$invalid }"
                :value="convertDateTimeFromServer($v.customer.lastModifiedDate.$model)"
                @change="updateInstantField('lastModifiedDate', $event)"
              />
            </div>
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
            :disabled="$v.customer.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./customer-update.component.ts"></script>
