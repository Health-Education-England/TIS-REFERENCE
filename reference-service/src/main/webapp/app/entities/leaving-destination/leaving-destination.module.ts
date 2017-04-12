import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	LeavingDestinationService,
	LeavingDestinationPopupService,
	LeavingDestinationComponent,
	LeavingDestinationDetailComponent,
	LeavingDestinationDialogComponent,
	LeavingDestinationPopupComponent,
	LeavingDestinationDeletePopupComponent,
	LeavingDestinationDeleteDialogComponent,
	leavingDestinationRoute,
	leavingDestinationPopupRoute,
	LeavingDestinationResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...leavingDestinationRoute,
	...leavingDestinationPopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		LeavingDestinationComponent,
		LeavingDestinationDetailComponent,
		LeavingDestinationDialogComponent,
		LeavingDestinationDeleteDialogComponent,
		LeavingDestinationPopupComponent,
		LeavingDestinationDeletePopupComponent,
	],
	entryComponents: [
		LeavingDestinationComponent,
		LeavingDestinationDialogComponent,
		LeavingDestinationPopupComponent,
		LeavingDestinationDeleteDialogComponent,
		LeavingDestinationDeletePopupComponent,
	],
	providers: [
		LeavingDestinationService,
		LeavingDestinationPopupService,
		LeavingDestinationResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceLeavingDestinationModule {
}
