import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {PlacementTypeDetailComponent} from "../../../../../../main/webapp/app/entities/placement-type/placement-type-detail.component";
import {PlacementTypeService} from "../../../../../../main/webapp/app/entities/placement-type/placement-type.service";
import {PlacementType} from "../../../../../../main/webapp/app/entities/placement-type/placement-type.model";

describe('Component Tests', () => {

	describe('PlacementType Management Detail Component', () => {
		let comp: PlacementTypeDetailComponent;
		let fixture: ComponentFixture<PlacementTypeDetailComponent>;
		let service: PlacementTypeService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [PlacementTypeDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					PlacementTypeService
				]
			}).overrideComponent(PlacementTypeDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(PlacementTypeDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(PlacementTypeService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new PlacementType(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.placementType).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
