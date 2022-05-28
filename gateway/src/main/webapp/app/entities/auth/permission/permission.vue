<template>
  <div>
    <h2 id="page-heading" data-cy="PermissionHeading">
      <span v-text="$t('gatewayApp.authPermission.home.title')" id="permission-heading">Permissions</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('gatewayApp.authPermission.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'PermissionCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-permission"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('gatewayApp.authPermission.home.createLabel')"> Create a new Permission </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && permissions && permissions.length === 0">
      <span v-text="$t('gatewayApp.authPermission.home.notFound')">No permissions found</span>
    </div>
    <div class="table-responsive" v-if="permissions && permissions.length > 0">
      <table class="table table-striped" aria-describedby="permissions">
        <thead>
          <tr>
            <th scope="row"><span v-text="$t('global.field.id')">ID</span></th>
            <th scope="row"><span v-text="$t('gatewayApp.authPermission.authKey')">Auth Key</span></th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="permission in permissions" :key="permission.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'PermissionView', params: { permissionId: permission.id } }">{{ permission.id }}</router-link>
            </td>
            <td>{{ permission.authKey }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'PermissionView', params: { permissionId: permission.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'PermissionEdit', params: { permissionId: permission.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(permission)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="gatewayApp.authPermission.delete.question" data-cy="permissionDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="au-delete-permission-heading" v-text="$t('gatewayApp.authPermission.delete.question', { id: removeId })">
          Are you sure you want to delete this Permission?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="au-confirm-delete-permission"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removePermission()"
        >
          Delete
        </button>
      </div>
    </b-modal>
  </div>
</template>

<script lang="ts" src="./permission.component.ts"></script>
