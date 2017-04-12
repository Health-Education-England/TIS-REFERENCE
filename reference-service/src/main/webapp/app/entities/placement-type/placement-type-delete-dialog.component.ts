import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, JhiLanguageService} from "ng-jhipster";
import {PlacementType} from "./placement-type.model";
import {PlacementTypePopupService} from "./placement-type-popup.service";
import {PlacementTypeService} from "./placement-type.service";

@Component({
	selector: 'jhi-placement-type-delete-dialog',
	templateUrl: './placement-type-delete-dialog.component.html'
})
export class PlacementTypeDeleteDialogComponent {

	placementType: PlacementType;

	constructor(private jhiLanguageService: JhiLanguageService,
				private placementTypeService: PlacementTypeService,
				public activeModal: NgbActiveModal,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['placementType']);
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	confirmDelete(id: number) {
		this.placementTypeService.delete(id).subscribe(response => {
			this.eventManager.broadcast({
				name: 'placementTypeListModification',
				content: 'Deleted an placementType'
			});
			this.activeModal.dismiss(true);
		});
	}
}

@Component({
	selector: 'jhi-placement-type-delete-popup',
	template: ''
})
export class PlacementTypeDeletePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private placementTypePopupService: PlacementTypePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			this.modalRef = this.placementTypePopupService
				.open(PlacementTypeDeleteDialogComponent, params['id']);
		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
