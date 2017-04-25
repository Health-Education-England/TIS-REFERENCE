import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	InactiveReasonService,
	InactiveReasonPopupService,
	InactiveReasonComponent,
	InactiveReasonDetailComponent,
	InactiveReasonDialogComponent,
	InactiveReasonPopupComponent,
	InactiveReasonDeletePopupComponent,
	InactiveReasonDeleteDialogComponent,
	inactiveReasonRoute,
	inactiveReasonPopupRoute,
	InactiveReasonResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...inactiveReasonRoute,
	...inactiveReasonPopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		InactiveReasonComponent,
		InactiveReasonDetailComponent,
		InactiveReasonDialogComponent,
		InactiveReasonDeleteDialogComponent,
		InactiveReasonPopupComponent,
		InactiveReasonDeletePopupComponent,
	],
	entryComponents: [
		InactiveReasonComponent,
		InactiveReasonDialogComponent,
		InactiveReasonPopupComponent,
		InactiveReasonDeleteDialogComponent,
		InactiveReasonDeletePopupComponent,
	],
	providers: [
		InactiveReasonService,
		InactiveReasonPopupService,
		InactiveReasonResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceInactiveReasonModule {
}
