import {Component, OnInit, OnDestroy} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Response} from "@angular/http";
import {NgbActiveModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {EventManager, AlertService, JhiLanguageService} from "ng-jhipster";
import {PlacementType} from "./placement-type.model";
import {PlacementTypePopupService} from "./placement-type-popup.service";
import {PlacementTypeService} from "./placement-type.service";
@Component({
	selector: 'jhi-placement-type-dialog',
	templateUrl: './placement-type-dialog.component.html'
})
export class PlacementTypeDialogComponent implements OnInit {

	placementType: PlacementType;
	authorities: any[];
	isSaving: boolean;

	constructor(public activeModal: NgbActiveModal,
				private jhiLanguageService: JhiLanguageService,
				private alertService: AlertService,
				private placementTypeService: PlacementTypeService,
				private eventManager: EventManager) {
		this.jhiLanguageService.setLocations(['placementType']);
	}

	ngOnInit() {
		this.isSaving = false;
		this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
	}

	clear() {
		this.activeModal.dismiss('cancel');
	}

	save() {
		this.isSaving = true;
		if (this.placementType.id !== undefined) {
			this.placementTypeService.update(this.placementType)
				.subscribe((res: PlacementType) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		} else {
			this.placementTypeService.create(this.placementType)
				.subscribe((res: PlacementType) =>
					this.onSaveSuccess(res), (res: Response) => this.onSaveError(res.json()));
		}
	}

	private onSaveSuccess(result: PlacementType) {
		this.eventManager.broadcast({name: 'placementTypeListModification', content: 'OK'});
		this.isSaving = false;
		this.activeModal.dismiss(result);
	}

	private onSaveError(error) {
		this.isSaving = false;
		this.onError(error);
	}

	private onError(error) {
		this.alertService.error(error.message, null, null);
	}
}

@Component({
	selector: 'jhi-placement-type-popup',
	template: ''
})
export class PlacementTypePopupComponent implements OnInit, OnDestroy {

	modalRef: NgbModalRef;
	routeSub: any;

	constructor(private route: ActivatedRoute,
				private placementTypePopupService: PlacementTypePopupService) {
	}

	ngOnInit() {
		this.routeSub = this.route.params.subscribe(params => {
			if (params['id']) {
				this.modalRef = this.placementTypePopupService
					.open(PlacementTypeDialogComponent, params['id']);
			} else {
				this.modalRef = this.placementTypePopupService
					.open(PlacementTypeDialogComponent);
			}

		});
	}

	ngOnDestroy() {
		this.routeSub.unsubscribe();
	}
}
