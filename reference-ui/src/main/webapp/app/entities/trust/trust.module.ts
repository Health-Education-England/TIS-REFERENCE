import {NgModule, CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";
import {RouterModule} from "@angular/router";
import {ReferenceSharedModule} from "../../shared";
import {
	TrustService,
	TrustPopupService,
	TrustComponent,
	TrustDetailComponent,
	TrustDialogComponent,
	TrustPopupComponent,
	TrustDeletePopupComponent,
	TrustDeleteDialogComponent,
	trustRoute,
	trustPopupRoute,
	TrustResolvePagingParams
} from "./";

let ENTITY_STATES = [
	...trustRoute,
	...trustPopupRoute,
];

@NgModule({
	imports: [
		ReferenceSharedModule,
		RouterModule.forRoot(ENTITY_STATES, {useHash: true})
	],
	declarations: [
		TrustComponent,
		TrustDetailComponent,
		TrustDialogComponent,
		TrustDeleteDialogComponent,
		TrustPopupComponent,
		TrustDeletePopupComponent,
	],
	entryComponents: [
		TrustComponent,
		TrustDialogComponent,
		TrustPopupComponent,
		TrustDeleteDialogComponent,
		TrustDeletePopupComponent,
	],
	providers: [
		TrustService,
		TrustPopupService,
		TrustResolvePagingParams,
	],
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ReferenceTrustModule {
}
